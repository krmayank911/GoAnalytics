package `in`.go.analytics_sdk.internals.configs

data class AnalyticsConfig(
    val batchSize: Int = 10,
    val flushIntervalMillis: Long = 30_000L
)