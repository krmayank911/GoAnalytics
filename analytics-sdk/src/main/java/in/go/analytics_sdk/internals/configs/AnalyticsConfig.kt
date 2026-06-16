package `in`.go.analytics_sdk.internals.configs

import kotlin.time.Duration.Companion.days

data class AnalyticsConfig(
    val batchSize: Int = 10,
    val flushIntervalMillis: Long = 30_000L,
    val maxEventAgeMillis: Long = 7.days.inWholeMilliseconds,
    val retryDelayMillis: Long = 5_000L,
    val flushBudget: FlushBudget = FlushBudget(),
)