package `in`.go.analytics_sdk

import android.content.Context
import android.util.Log
import `in`.go.analytics_sdk.internals.AnalyticsConfig
import `in`.go.analytics_sdk.internals.AnalyticsManager
import `in`.go.analytics_sdk.internals.InMemoryEventStore
import `in`.go.analytics_sdk.internals.LogCatUploader
import `in`.go.analytics_sdk.internals.SessionManager
import `in`.go.analytics_sdk.internals.UserManager
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

        analyticsManager = AnalyticsManager(
            store = InMemoryEventStore(),
            sessionManager = SessionManager(),
            userManager = UserManager(),
            uploader = LogCatUploader(),
            analyticsConfig = config
        )
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