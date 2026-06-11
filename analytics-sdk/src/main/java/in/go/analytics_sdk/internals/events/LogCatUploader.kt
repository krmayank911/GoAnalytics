package `in`.go.analytics_sdk.internals.events

import android.util.Log

internal class LogCatUploader: EventUploader {

    override fun upload(events: List<AnalyticsEvent>): Boolean {
        events.forEach {
            Log.d("AnalyticsSDK", it.toString())
        }
        return true
    }

}