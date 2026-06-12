package `in`.go.analytics_sdk.internals.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal object NetworkClient {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.example.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getAnalyticsApi(): AnalyticsApi {
        return retrofit.create(AnalyticsApi::class.java)
    }

}