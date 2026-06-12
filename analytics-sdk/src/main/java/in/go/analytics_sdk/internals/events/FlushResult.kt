package `in`.go.analytics_sdk.internals.events

sealed interface FlushResult {

    object Completed: FlushResult

    data class RetryLater(val delayMillis: Long) : FlushResult
}
