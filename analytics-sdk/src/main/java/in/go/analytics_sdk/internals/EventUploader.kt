package `in`.go.analytics_sdk.internals

import android.util.Log

internal interface EventUploader {

    fun upload(events: List<AnalyticsEvent>): Boolean

}

internal class LogCatUploader: EventUploader {

    override fun upload(events: List<AnalyticsEvent>): Boolean {
        events.forEach {
            Log.d("AnalyticsSDK", it.toString())
        }
        return true
    }

}