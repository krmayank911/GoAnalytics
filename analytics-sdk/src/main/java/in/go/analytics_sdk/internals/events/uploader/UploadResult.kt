package `in`.go.analytics_sdk.internals.events.uploader

internal sealed interface UploadResult {
    object UploadSuccess: UploadResult

    data class RetryableFailure(
        val retryAfter: Long? = null,
        val reason: Throwable? = null
    ): UploadResult

    data class PermanentFailure(
        val reason: Throwable? = null
    ): UploadResult
}