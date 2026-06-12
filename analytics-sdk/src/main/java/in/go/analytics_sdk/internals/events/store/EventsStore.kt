package `in`.go.analytics_sdk.internals.events.store

import `in`.go.analytics_sdk.internals.events.AnalyticsEvent

internal interface EventStore {
    suspend fun add(event: AnalyticsEvent)
    suspend fun getAll(): List<AnalyticsEvent>
    suspend fun getBatch(batchSize: Int): List<AnalyticsEvent>
    suspend fun size(): Int
    suspend fun remove(events: List<AnalyticsEvent>)
}