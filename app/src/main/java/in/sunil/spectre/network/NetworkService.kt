package `in`.sunil.spectre.network

import `in`.sunil.spectre.network.api.artist.ArtistDetailResponse
import `in`.sunil.spectre.network.api.search.SearchResponse
import `in`.sunil.spectre.util.toClassData
import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor

/**
 * Created by Sunil on 10/4/18.
 */
class NetworkService {

    companion object {

        val TAG = NetworkService::class.java.simpleName

        const val SPOTIFY_BASE_URL = "https://api.spotify.com/v1"

    }

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

    fun getSearchQuery(query: String): SearchResponse? {

        val url = "$SPOTIFY_BASE_URL/search?q=$query&type=album,track"

        val request = Request.Builder().url(url).build()

        val response = okHttpClient.newCall(request).execute()

        val searchResponse = response.body()?.string()?.toClassData(SearchResponse::class.java)

        return searchResponse
    }

    fun getArtistDetail(artistID: String): ArtistDetailResponse? {

        val url = "$SPOTIFY_BASE_URL/artists/$artistID"

        val request = Request.Builder().url(url).build()

        val response = okHttpClient.newCall(request).execute()

        val artistDetailResponse = response.body()?.string()?.toClassData(ArtistDetailResponse::class.java)

        return artistDetailResponse
    }
}