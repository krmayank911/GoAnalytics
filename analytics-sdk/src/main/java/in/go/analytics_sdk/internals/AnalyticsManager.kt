package `in`.go.analytics_sdk.internals

import android.util.Log
import `in`.go.analytics_sdk.internals.configs.AnalyticsConfig
import `in`.go.analytics_sdk.internals.events.AnalyticsEvent
import `in`.go.analytics_sdk.internals.events.FlushResult
import `in`.go.analytics_sdk.internals.events.store.EventStore
import `in`.go.analytics_sdk.internals.events.uploader.EventUploader
import `in`.go.analytics_sdk.internals.events.uploader.UploadResult
import `in`.go.analytics_sdk.internals.identity.SessionManager
import `in`.go.analytics_sdk.internals.identity.UserManager
import `in`.go.analytics_sdk.internals.network.UploadFailureRetry
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class AnalyticsManager(
    dependencies: AnalyticsDependencies
) {

    val userManager: UserManager = dependencies.userManager
    private val store: EventStore = dependencies.store
    private val sessionManager: SessionManager = dependencies.sessionManager
    private val uploader: EventUploader = dependencies.uploader
    private val analyticsConfig: AnalyticsConfig = dependencies.config
    private val dispatcher: CoroutineDispatcher = dependencies.dispatcher
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + dispatcher)
    private val flushMutex = Mutex()
    private var flushJob: Job? = null
    private var retryCount = 0
    private var flushBlockedUntil = 0L
    private val failureRetry by lazy {
        UploadFailureRetry(minRetryDelayMillis = analyticsConfig.retryDelayMillis)
    }

    init {
        scope.launch {
            while (isActive) {
                delay(analyticsConfig.flushIntervalMillis)
                flushAsync()
            }
        }
    }

    fun track(event: String, properties: Map<String, Any>?) = scope.launch {
        store.add(
            AnalyticsEvent(
                event = event,
                properties = properties ?: emptyMap(),
                timestamp = System.currentTimeMillis(),
                sessionId = sessionManager.sessionId,
                userId = userManager.userId
            )
        )

        if (store.size() >= analyticsConfig.batchSize) {
            flushAsync()
        }
    }

    fun flushAsync() {
        if (flushBlockedUntil > System.currentTimeMillis()) {
            return
        }
        if (flushJob?.isActive == true) {
            return
        }
        flushJob = scope.launch {
            when (val result = flush()) {
                is FlushResult.Completed -> {}
                is FlushResult.RetryLater -> {
                    flushBlockedUntil = System.currentTimeMillis() + result.delayMillis
                }
            }
        }
    }

    suspend fun flush(): FlushResult {
        flushMutex.withLock {
            while(store.size() > 0) {
                val batch = store.getBatch(analyticsConfig.batchSize)
                when (val result = uploader.upload(batch)) {
                    is UploadResult.UploadSuccess -> {
                        logInfo("Upload Success: ${batch.size} events uploaded successfully")
                        retryCount = 0
                    }
                    is UploadResult.RetryableFailure -> {
                        logInfo("Network Error: ${result.reason?.message}")
                        retryCount++

                        return if (failureRetry.shouldRetry(result)) {
                            FlushResult.RetryLater(
                                result.retryAfter ?: failureRetry.nextDelayMillis(retryCount)
                            )
                        } else {
                            FlushResult.Completed
                        }
                    }
                    is UploadResult.PermanentFailure -> {
                        logInfo("Network Error: ${result.reason?.message}")
                        retryCount = 0
                    }
                }
                store.remove(batch)
            }
        }
        return FlushResult.Completed
    }

    private fun logInfo(message: String) {
        Log.i("AnalyticsSDK", message)
    }

}