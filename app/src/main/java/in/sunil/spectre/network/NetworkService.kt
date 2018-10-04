package `in`.sunil.spectre.network

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor

/**
 * Created by Sunil on 10/4/18.
 */
class NetworkService {

    private var accessToken: String? = ""
    private val okHttpClient: OkHttpClient

    constructor(context: Context, okHttpClient: OkHttpClient) {

        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        this.okHttpClient = okHttpClient.newBuilder()
                .addInterceptor { chain ->

                    val originalRequest = chain.request()
                    val requestBuilder = originalRequest.newBuilder()

                    val authHeader = "Bearer $accessToken"
                    requestBuilder.addHeader("Authorization", authHeader)
                    chain.proceed(requestBuilder.build())
                }
                .addInterceptor(httpLoggingInterceptor)
                .build()
    }

    fun setAccessToken(accessToken: String?) {
        this.accessToken = accessToken
    }

    fun getSearchQuery(query: String): String? {

        val url = "https://api.spotify.com/v1/search?q=$query&type=album,track"

        val request = Request.Builder().url(url).build()

        val response = okHttpClient.newCall(request).execute()
        val inputStream = response.body()?.string()

        return inputStream
    }
}