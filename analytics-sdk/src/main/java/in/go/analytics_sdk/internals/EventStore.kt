package `in`.go.analytics_sdk.internals

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal interface EventStore {
    suspend fun add(event: AnalyticsEvent)
    suspend fun getAll(): List<AnalyticsEvent>
    suspend fun getBatch(batchSize: Int): List<AnalyticsEvent>
    suspend fun size(): Int
    suspend fun remove(events: List<AnalyticsEvent>)
}

internal class InMemoryEventStore : EventStore {

    private val events = mutableListOf<AnalyticsEvent>()
    private val eventMutex = Mutex()

    override suspend fun add(event: AnalyticsEvent) {
        eventMutex.withLock {
            events.add(event)
        }
    }

    override suspend fun getAll(): List<AnalyticsEvent> {
        return eventMutex.withLock {
            events
        }
    }

    override suspend fun getBatch(batchSize: Int): List<AnalyticsEvent> {
        return eventMutex.withLock {
            events.take(batchSize)
        }
    }

    override suspend fun size(): Int {
        return eventMutex.withLock {
            events.size
        }
    }

    override suspend fun remove(events: List<AnalyticsEvent>) {
        eventMutex.withLock {
            events.forEach { event ->
                this.events.remove(event)
            }
        }
    }
}