package `in`.go.analytics

import android.app.Activity
import android.app.Application
import android.os.Bundle
import `in`.go.analytics.Events.APP_CLOSED
import `in`.go.analytics.Events.APP_OPENED
import `in`.go.analytics_sdk.GoAnalytics
import `in`.go.analytics_sdk.internals.AnalyticsConfig

class TestApplication: Application(), Application.ActivityLifecycleCallbacks {

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)

        GoAnalytics.initialize(
            context = this,
            apiKey = "YOUR_API_KEY",
            config = AnalyticsConfig(
                batchSize = 10
            )
        )
        GoAnalytics.identify("123")
    }

    override fun onActivityCreated(
        activity: Activity,
        savedInstanceState: Bundle?
    ) {
        GoAnalytics.track(APP_OPENED)
    }

    override fun onActivityDestroyed(activity: Activity) {
        GoAnalytics.track(APP_CLOSED)
        GoAnalytics.flush()
    }

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityStarted(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

}
