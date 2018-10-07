package `in`.sunil.spectre.network

import `in`.sunil.spectre.network.api.artist.ArtistDetailResponse
import `in`.sunil.spectre.network.api.search.SearchResponse
import `in`.sunil.spectre.network.api.toptracks.ArtistTopAlbumsResponse
import `in`.sunil.spectre.util.Utilities
import `in`.sunil.spectre.util.toClassData
import android.content.Context
import io.reactivex.Flowable
import io.reactivex.processors.PublishProcessor
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.Callable

/**
 * Created by Sunil on 10/4/18.
 */
class NetworkService : INetworkService {

    companion object {

        val TAG = NetworkService::class.java.simpleName

        const val SPOTIFY_BASE_URL = "https://api.spotify.com/v1"
        var cacheSize = 1 * 1024 * 1024L // 1 MB
    }

    private var accessToken: String? = ""

    private val context: Context
    private val okHttpClient: OkHttpClient

    private val networkChangeSubject = PublishProcessor.create<Boolean>()

    constructor(context: Context, okHttpClient: OkHttpClient) {

        this.context = context

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

    override fun setAccessToken(accessToken: String?) {
        this.accessToken = accessToken
    }

    override fun setNetworkChanged() {

        networkChangeSubject.onNext(Utilities.isNetworkAvailable(context))
    }

    override fun subscribeNetworkChangeSubject(): Flowable<Boolean> {

        return networkChangeSubject.hide().distinctUntilChanged()
    }


    override fun getSearchQueryFlowable(query: String): Flowable<SearchResponse> {

        return Flowable.fromCallable(Callable {

            val url = "$SPOTIFY_BASE_URL/search?q=$query&type=album,track"

            val request = Request.Builder().url(url).build()
            val response = okHttpClient.newCall(request).execute()

            return@Callable response.body()?.string()?.toClassData(SearchResponse::class.java)
        })
    }

    override fun getArtistDetailFlowable(artistID: String): Flowable<ArtistDetailResponse> {

        return Flowable.fromCallable(Callable {

            val url = "$SPOTIFY_BASE_URL/artists/$artistID"

            val request = Request.Builder().url(url).build()
            val response = okHttpClient.newCall(request).execute()

            return@Callable response.body()?.string()?.toClassData(ArtistDetailResponse::class.java)
        })
    }


    override fun getArtistTopAlbumsFlowable(artistID: String): Flowable<ArtistTopAlbumsResponse> {

        return Flowable.fromCallable(Callable {

            val url = "$SPOTIFY_BASE_URL/artists/$artistID/albums"

            val request = Request.Builder().url(url).build()
            val response = okHttpClient.newCall(request).execute()

            return@Callable response.body()?.string()?.toClassData(ArtistTopAlbumsResponse::class.java)
        })
    }

}