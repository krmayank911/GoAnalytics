package `in`.go.analytics_sdk.internals.network

import `in`.go.analytics_sdk.internals.events.uploader.UploadResult
import kotlin.math.min

internal class UploadFailureRetry(val minRetryDelayMillis: Long): RetryPolicy {

    companion object {
        const val MAX_BACKOFF_MILLIS = 30_000L
    }

    init {
        require(minRetryDelayMillis > 0) { "Delay must be greater than zero" }
    }

    override fun shouldRetry(result: UploadResult): Boolean {
        return result is UploadResult.RetryableFailure
    }

    override fun nextDelayMillis(retryCount: Int): Long {
        return min(
            minRetryDelayMillis * (1 shl retryCount),
            MAX_BACKOFF_MILLIS
        )
    }
}