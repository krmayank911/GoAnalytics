package `in`.go.analytics_sdk.internals.events

internal data class AnalyticsEvent(
    val event: String,
    val properties: Map<String, Any>,
    val timestamp: Long,
    val sessionId: String,
    val userId: String?
)