package `in`.go.analytics_sdk.internals.events

internal interface EventUploader {

    fun upload(events: List<AnalyticsEvent>): Boolean

}