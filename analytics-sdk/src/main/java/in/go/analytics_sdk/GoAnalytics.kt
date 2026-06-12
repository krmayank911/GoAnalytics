package `in`.go.analytics_sdk

import android.content.Context
import android.util.Log
import `in`.go.analytics_sdk.internals.AnalyticsFactory
import `in`.go.analytics_sdk.internals.configs.AnalyticsConfig
import `in`.go.analytics_sdk.internals.AnalyticsManager
import kotlin.isInitialized

object GoAnalytics {

    private lateinit var analyticsManager: AnalyticsManager

    @Synchronized
    fun initialize(
        context: Context,
        apiKey: String,
        config: AnalyticsConfig
    ) {

        if (::analyticsManager.isInitialized) {
            Log.w("AnalyticsSDK", "Analytics SDK already initialized")
            Log.w("AnalyticsSDK", "Ignoring duplicate initialize() call.")
            return
        }
        analyticsManager = AnalyticsFactory.create(context, config)
    }

    fun identify(userId: String) {
        requireInitialized()
        analyticsManager.userManager.identify(userId)
    }

    private fun requireInitialized() {
        check(::analyticsManager.isInitialized) {
            "GoAnalytics has not been initialized. You need to call GoAnalytics.initialize() before using it."
        }
    }

    fun track(event: String, properties: Map<String, Any>? = null) {
        requireInitialized()
        analyticsManager.track(event, properties)
    }

    fun flush() {
        analyticsManager.flushAsync()
    }

}