package `in`.go.analytics_sdk.internals.network

import `in`.go.analytics_sdk.internals.events.uploader.UploadResult

internal interface RetryPolicy {

    fun shouldRetry(result: UploadResult): Boolean

    fun nextDelayMillis(retryCount: Int): Long
}