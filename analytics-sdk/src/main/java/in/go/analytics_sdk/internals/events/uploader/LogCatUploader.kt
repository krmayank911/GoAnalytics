package `in`.go.analytics_sdk.internals.events.uploader

import android.util.Log
import `in`.go.analytics_sdk.internals.events.AnalyticsEvent
import `in`.go.analytics_sdk.internals.events.uploader.EventUploader

internal class LogCatUploader: EventUploader {

    override suspend fun upload(events: List<AnalyticsEvent>): UploadResult {
        events.forEach {
            Log.d("AnalyticsSDK", it.toString())
        }
        return UploadResult.UploadSuccess
    }

}