package `in`.sunil.spectre.network

import `in`.sunil.spectre.network.api.artist.ArtistDetailResponse
import `in`.sunil.spectre.network.api.search.SearchResponse
import `in`.sunil.spectre.network.api.toptracks.ArtistTopAlbumsResponse
import `in`.sunil.spectre.util.toClassData
import android.content.Context
import android.util.Log
import io.reactivex.Flowable
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.Callable

/**
 * Created by Sunil on 10/4/18.
 */
class NetworkService {

    companion object {

        val TAG = NetworkService::class.java.simpleName

        const val SPOTIFY_BASE_URL = "https://api.spotify.com/v1"
        var cacheSize = 1 * 1024 * 1024L // 1 MB
    }

    private var accessToken: String? = ""
    private val okHttpClient: OkHttpClient

    constructor(context: Context, okHttpClient: OkHttpClient) {

        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        this.okHttpClient = okHttpClient.newBuilder()
                .cache(Cache(context.cacheDir, cacheSize))
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

    fun getSearchQueryFlowable(query: String): Flowable<SearchResponse> {

        return Flowable.fromCallable(Callable {

            val url = "$SPOTIFY_BASE_URL/search?q=$query&type=album,track"

            val request = Request.Builder()
                    .url(url).build()

            val response = okHttpClient.newCall(request).execute()

            if (response.networkResponse() == null) {
                Log.d(TAG, "Testing4 Okhttp: Cache")
            } else if (response.cacheResponse() == null) {
                Log.d(TAG, "Testing4 okhttp : Network")
            }

            val searchResponse = response.body()?.string()?.toClassData(SearchResponse::class.java)
            return@Callable searchResponse
        })
    }

    fun getArtistDetailFlowable(artistID: String): Flowable<ArtistDetailResponse> {

        return Flowable.fromCallable(Callable {

            val url = "$SPOTIFY_BASE_URL/artists/$artistID"

            val request = Request.Builder().url(url).build()

            val response = okHttpClient.newCall(request).execute()

            val artistDetailResponse = response.body()?.string()?.toClassData(ArtistDetailResponse::class.java)

            return@Callable artistDetailResponse
        })
    }


    fun getArtistTopAlbumsFlowable(artistID: String): Flowable<ArtistTopAlbumsResponse> {

        return Flowable.fromCallable(Callable {

            val url = "$SPOTIFY_BASE_URL/artists/$artistID/albums"

            val request = Request.Builder().url(url).build()

            val response = okHttpClient.newCall(request).execute()

            val topAlbumsResponse = response.body()?.string()?.toClassData(ArtistTopAlbumsResponse::class.java)

            return@Callable topAlbumsResponse
        })
    }


}