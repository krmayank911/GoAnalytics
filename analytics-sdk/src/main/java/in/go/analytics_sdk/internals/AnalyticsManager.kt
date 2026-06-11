package `in`.go.analytics_sdk.internals

import `in`.go.analytics_sdk.internals.configs.AnalyticsConfig
import `in`.go.analytics_sdk.internals.events.AnalyticsEvent
import `in`.go.analytics_sdk.internals.events.EventStore
import `in`.go.analytics_sdk.internals.events.EventUploader
import `in`.go.analytics_sdk.internals.identity.SessionManager
import `in`.go.analytics_sdk.internals.identity.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class AnalyticsManager(
    val store: EventStore,
    val sessionManager: SessionManager,
    val userManager: UserManager,
    val uploader: EventUploader,
    val analyticsConfig: AnalyticsConfig
) {

    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val flushMutex = Mutex()
    private var flushJob: Job? = null

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
        if (flushJob?.isActive == true) {
            return
        }
        flushJob = scope.launch {
            flush()
        }
    }

    suspend fun flush() {
        flushMutex.withLock {
            while(store.size() > 0) {
                val batch = store.getBatch(analyticsConfig.batchSize)
                if (!uploader.upload(batch)) {
                    break
                }
                store.remove(batch)
            }
        }
    }

}