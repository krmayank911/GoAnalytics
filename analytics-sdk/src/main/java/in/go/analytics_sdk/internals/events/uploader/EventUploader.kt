package `in`.go.analytics_sdk.internals.events.uploader

import `in`.go.analytics_sdk.internals.events.AnalyticsEvent

internal interface EventUploader {

    suspend fun upload(events: List<AnalyticsEvent>): UploadResult

}