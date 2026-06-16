package `in`.go.analytics_sdk.internals.configs

data class FlushBudget(
    val maxDurationMillis: Long = 5_000,
    val maxBatches: Int = 10
)
