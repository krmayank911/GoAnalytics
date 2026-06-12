package `in`.go.analytics_sdk.internals.database

import android.content.Context
import androidx.room.Database
import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import `in`.go.analytics_sdk.internals.events.AnalyticsEvent

@Database(
    entities = [AnalyticsEvent::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(EventTypeConverter::class)
internal abstract class EventsDataBase(): RoomDatabase() {

    abstract fun eventsDao(): EventsDao

    companion object {
        @Volatile
        private var INSTANCE: EventsDataBase? = null

        fun getDatabase(context: Context): EventsDataBase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context,
                    EventsDataBase::class.java,
                    "events_database"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }

    override fun clearAllTables() {}

    override fun createInvalidationTracker(): InvalidationTracker {
        return InvalidationTracker(
            database = this,
        )
    }

    override fun createOpenHelper(config: DatabaseConfiguration): SupportSQLiteOpenHelper {
        val sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context)
            .name(config.name)
            .build()
        return FrameworkSQLiteOpenHelperFactory().create(sqliteConfig)
    }

}