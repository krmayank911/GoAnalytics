package `in`.go.analytics_sdk.internals.network

import `in`.go.analytics_sdk.internals.events.uploader.UploadRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

internal interface AnalyticsApi {

    @POST("/syncEvents")
    suspend fun uploadEvents(@Body request: UploadRequest): Response<Unit>

}