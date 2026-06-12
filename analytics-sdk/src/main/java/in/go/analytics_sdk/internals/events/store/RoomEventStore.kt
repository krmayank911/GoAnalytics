package `in`.go.analytics_sdk.internals.events.store

import `in`.go.analytics_sdk.internals.database.EventsDao
import `in`.go.analytics_sdk.internals.events.AnalyticsEvent
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class RoomEventStore(val dao: EventsDao): EventStore {
    private val eventMutex = Mutex()

    override suspend fun add(event: AnalyticsEvent) {
        eventMutex.withLock {
            dao.insert(event)
        }
    }

    override suspend fun getAll(): List<AnalyticsEvent> {
        return eventMutex.withLock {
            dao.getAllEvents()
        }
    }

    override suspend fun getBatch(batchSize: Int): List<AnalyticsEvent> {
        return eventMutex.withLock {
            dao.getEvents(batchSize)
        }
    }

    override suspend fun size(): Int {
        return eventMutex.withLock {
            dao.getEventCount()
        }
    }

    override suspend fun remove(events: List<AnalyticsEvent>) {
        return eventMutex.withLock {
            dao.deleteEvents(events.map { it.id })
        }
    }
}