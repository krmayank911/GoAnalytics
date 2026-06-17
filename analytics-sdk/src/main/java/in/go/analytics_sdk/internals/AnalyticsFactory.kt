package `in`.go.analytics_sdk.internals

import android.content.Context
import `in`.go.analytics_sdk.internals.configs.AnalyticsConfig
import `in`.go.analytics_sdk.internals.configs.AnalyticsConfigStore
import `in`.go.analytics_sdk.internals.configs.GoClock
import `in`.go.analytics_sdk.internals.configs.SystemClock
import `in`.go.analytics_sdk.internals.database.AnalyticsDatabase
import `in`.go.analytics_sdk.internals.events.store.EventStore
import `in`.go.analytics_sdk.internals.events.uploader.EventUploader
import `in`.go.analytics_sdk.internals.events.store.InMemoryEventStore
import `in`.go.analytics_sdk.internals.events.uploader.LogCatUploader
import `in`.go.analytics_sdk.internals.events.store.RoomEventStore
import `in`.go.analytics_sdk.internals.network.AnalyticsApi
import `in`.go.analytics_sdk.internals.events.uploader.RetrofitUploader
import `in`.go.analytics_sdk.internals.events.uploader.scheduler.SyncSchedular
import `in`.go.analytics_sdk.internals.events.uploader.scheduler.WorkManagerScheduler
import `in`.go.analytics_sdk.internals.identity.SessionManager
import `in`.go.analytics_sdk.internals.identity.UserManager
import `in`.go.analytics_sdk.internals.network.NetworkClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

internal object AnalyticsFactory {

    fun create(
        context: Context,
        config: AnalyticsConfig
    ): AnalyticsManager {
        val database = AnalyticsDatabase(context)

        return AnalyticsManager(
            dependencies = AnalyticsDependencies(
                store = RoomEventStore(database.eventsDao()),
                uploader = RetrofitUploader(api = NetworkClient.getAnalyticsApi()),
                config = config,
                dispatcher = Dispatchers.IO,
                clock = SystemClock(),
                scheduler = WorkManagerScheduler(context)
            )
        )
    }
}

internal data class AnalyticsDependencies(
    val store: EventStore = InMemoryEventStore(),
    val uploader: EventUploader = LogCatUploader(),
    val sessionManager: SessionManager = SessionManager(),
    val userManager: UserManager = UserManager(),
    val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    val config: AnalyticsConfig = AnalyticsConfig(),
    val clock: GoClock = SystemClock(),
    val scheduler: SyncSchedular? = null
)