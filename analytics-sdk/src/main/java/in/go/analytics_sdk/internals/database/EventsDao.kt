package `in`.go.analytics_sdk.internals.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import `in`.go.analytics_sdk.internals.events.AnalyticsEvent

@Dao
internal interface EventsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: AnalyticsEvent)

    @Query("SELECT * FROM events LIMIT :limit")
    suspend fun getEvents(limit: Int): List<AnalyticsEvent>

    @Query("SELECT * FROM events")
    suspend fun getAllEvents(): List<AnalyticsEvent>

    @Query("SELECT COUNT(*) FROM events")
    suspend fun getEventCount(): Int

    @Query("DELETE FROM events WHERE id IN (:ids)")
    suspend fun deleteEvents(ids: List<String>)

}