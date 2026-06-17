package `in`.go.analytics_sdk.internals.configs

import android.content.Context

internal object AnalyticsConfigStore {

    private const val PREF = "analytics_sdk"
    private const val KEY_BATCH = "batch"
    private const val FLUSH_BUDGET_MAX_BATCHES = "flushBudgetMaxBatches"
    private const val FLUSH_BUDGET_MAX_DURATION = "flushBudgetMaxDuration"

    fun save(
        context: Context,
        config: AnalyticsConfig
    ) {
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .edit()
            .putInt(KEY_BATCH, config.batchSize)
            .putInt(FLUSH_BUDGET_MAX_BATCHES, config.flushBudget.maxBatches)
            .putLong(FLUSH_BUDGET_MAX_DURATION, config.flushBudget.maxDurationMillis)
            .apply()
    }

    fun load(context: Context): AnalyticsConfig {
        val pref = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        val batchSize = pref.getInt(KEY_BATCH, 20)
        val flushBudgetMaxBatches = pref.getInt(FLUSH_BUDGET_MAX_BATCHES, 20)
        val flushBudgetMaxDuration = pref.getLong(FLUSH_BUDGET_MAX_DURATION, 10_000L)

        return AnalyticsConfig(
            batchSize = batchSize,
            flushBudget = FlushBudget(
                maxBatches = flushBudgetMaxBatches,
                maxDurationMillis = flushBudgetMaxDuration
            )
        )
    }

}