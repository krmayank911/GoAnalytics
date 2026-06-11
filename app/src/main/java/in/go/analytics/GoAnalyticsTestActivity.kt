package `in`.go.analytics

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import `in`.go.analytics.Events.BUTTON_CLICKED
import `in`.go.analytics.Events.SCREEN_DESTROYED
import `in`.go.analytics.Events.SCREEN_VIEWED
import `in`.go.analytics_sdk.GoAnalytics

class GoAnalyticsTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_go_analytics_test)

        GoAnalytics.track(SCREEN_VIEWED)

        findViewById<Button>(R.id.button).setOnClickListener {
            GoAnalytics.track(BUTTON_CLICKED, mapOf("button_text" to "Click Me"))
        }
    }

    override fun onStop() {
        super.onStop()
        GoAnalytics.track(SCREEN_DESTROYED)
    }

}
