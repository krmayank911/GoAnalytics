package `in`.go.analytics_sdk.internals.database

import android.content.Context

internal class AnalyticsDatabase(val context: Context) : EventsDataBase() {

    override fun eventsDao(): EventsDao {
        return getDatabase(context).eventsDao()
    }
}