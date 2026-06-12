package `in`.go.analytics_sdk.internals.events.uploader

import `in`.go.analytics_sdk.internals.events.AnalyticsEvent
import `in`.go.analytics_sdk.internals.network.AnalyticsApi
import `in`.go.analytics_sdk.internals.network.PERMANENT_FAILURE_STATUS_CODES
import `in`.go.analytics_sdk.internals.network.RETRYABLE_STATUS_CODES
import `in`.go.analytics_sdk.internals.network.TOO_MANY_REQUESTS
import java.io.IOException

internal class RetrofitUploader(
    val api: AnalyticsApi
): EventUploader {

    override suspend fun upload(events: List<AnalyticsEvent>): UploadResult {
        return try {
            val response =  api.uploadEvents(
                request = UploadRequest(events)
            )
            when {
                response.isSuccessful -> {
                    UploadResult.UploadSuccess
                }
                response.code() in RETRYABLE_STATUS_CODES -> {
                    UploadResult.RetryableFailure(
                        reason = Exception("Server Error: ${response.code()}")
                    )
                }
                response.code() == TOO_MANY_REQUESTS -> {
                    UploadResult.RetryableFailure(
                        retryAfter = response.headers()["Retry-After"]?.toLongOrNull(),
                        reason = Exception("Rate Limit Exceeded: ${response.code()}")
                    )
                }
                response.code() in PERMANENT_FAILURE_STATUS_CODES -> {
                    UploadResult.PermanentFailure(
                        reason = Exception("Server Error: ${response.code()}")
                    )
                }
                else -> {
                    UploadResult.PermanentFailure(
                        reason = Exception("Unknown Error: ${response.code()}")
                    )
                }
            }
        } catch (e: IOException) {
            UploadResult.RetryableFailure(reason = e)
        } catch (e: Exception) {
            UploadResult.PermanentFailure(reason = e)
        }
    }

}