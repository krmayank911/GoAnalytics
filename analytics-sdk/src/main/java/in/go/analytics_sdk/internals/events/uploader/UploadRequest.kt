package `in`.go.analytics_sdk.internals.events.uploader

import `in`.go.analytics_sdk.internals.events.AnalyticsEvent

internal data class UploadRequest(
    val events: List<AnalyticsEvent>
)
