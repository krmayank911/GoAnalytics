package `in`.go.analytics_sdk.internals.events

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "events")
internal data class AnalyticsEvent(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val event: String,
    val properties: Map<String, Any>,
    val timestamp: Long,
    val sessionId: String,
    val userId: String?
)