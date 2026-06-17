package `in`.go.analytics_sdk.internals.events.uploader.scheduler

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

internal class WorkManagerScheduler(val context: Context): SyncSchedular {

    override fun schedule() {

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<AnalyticsWorker>()
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context = context)
            .enqueueUniqueWork(
                uniqueWorkName = "analytics_sync",
                existingWorkPolicy = ExistingWorkPolicy.KEEP,
                request = workRequest
            )
    }
}