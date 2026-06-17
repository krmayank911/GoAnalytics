package `in`.go.analytics_sdk.internals.events.uploader.scheduler

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import `in`.go.analytics_sdk.GoAnalytics
import `in`.go.analytics_sdk.internals.AnalyticsFactory
import `in`.go.analytics_sdk.internals.configs.AnalyticsConfigStore
import java.lang.Exception

class AnalyticsWorker(
    val appContext : Context,
    val params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        return try {

            if (GoAnalytics.isInitialized()) return Result.success()

            val config = AnalyticsConfigStore.load(context = appContext)
            val manager = AnalyticsFactory.create(
                appContext,
                config
            )
            manager.flushAsync()
            Result.success()
        } catch (e: Exception) {
            Result.success()
        }
    }

}