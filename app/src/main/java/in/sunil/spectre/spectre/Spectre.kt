package `in`.sunil.spectre.spectre

import `in`.sunil.spectre.network.OkHttpDownloadProgressListener
import `in`.sunil.spectre.network.OkHttpDownloadProgressResponseBody
import `in`.sunil.spectre.spectre.cache.DiskLruCache
import `in`.sunil.spectre.spectre.cache.MemoryLruCache
import `in`.sunil.spectre.util.circleCentreCrop
import `in`.sunil.spectre.util.workOnBackgroundThread
import `in`.sunil.spectre.util.workOnMainThread
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import okhttp3.OkHttpClient
import okhttp3.Request


/**
 * Created by Sunil on 26/09/17.
 */


/*
   Spectre is a image loading class which uses a memory and disk cache for
   faster access to images
 */
class Spectre {

    companion object {

        val TAG = Spectre::class.java.simpleName
        var DISK_CACHE_SIZE = 5 * 1024 * 1024L //5mb

        val MEMORY_CACHE_SIZE = (Runtime.getRuntime().maxMemory() / 10)

        const val MAX_MEMORY_CACHE_ENTRY = 2
        const val MAX_DISK_CACHE_ENTRY = 4
    }

    private val okHttpClient: OkHttpClient
    private val diskLruCache: DiskLruCache
    private val memoryLruCache: MemoryLruCache

    private var disposable: CompositeDisposable? = null

    private var progressSubject = PublishSubject.create<Int>()

    constructor(context: Context, okHttpClient: OkHttpClient) {

        this.okHttpClient = okHttpClient.newBuilder()
                .addNetworkInterceptor { chain ->
                    val request = chain.request()
                    val originalResponse = chain.proceed(request)
                    originalResponse.newBuilder()
                            .body(OkHttpDownloadProgressResponseBody(request.url().toString(), originalResponse.body(),
                                    object : OkHttpDownloadProgressListener {
                                        override fun progress(url: String, progress: Int, complete: Boolean) {
                                            progressSubject.onNext(progress)
                                        }
                                    }))
                            .build()
                }
                .build()

        diskLruCache = DiskLruCache(context, MAX_DISK_CACHE_ENTRY, DISK_CACHE_SIZE)
        memoryLruCache = MemoryLruCache(MAX_MEMORY_CACHE_ENTRY, MEMORY_CACHE_SIZE)
    }

    /*
         Loads image into a imageview according to the following steps
          1) Checks if image is present in memory cache
          2) If not Checks if image is present in disk memory cache
          3) Else Download image from network and save to memory and disk cache
     */

    fun loadImage(imageView: ImageView, spectreOptions: SpectreOptions, onError: (() -> Unit)?) {

        disposable?.dispose()
        disposable = CompositeDisposable()

        disposable?.add(workOnBackgroundThread({

            spectreOptions.progressListener?.invoke(0)

            var bitmap = memoryLruCache.getBitmapFromMemory(spectreOptions.imageUrl)

            if (bitmap == null) {
                bitmap = diskLruCache.getBitmapFromDisk(spectreOptions.imageUrl)
            }

            //Image is not present in cache so downloading the image from network
            if (bitmap == null) {

                // Showing placeholder only when loading from network
                spectreOptions.placeHolder?.let { placeHolder ->

                    val placeholder = BitmapFactory.decodeResource(imageView.resources, placeHolder)
                    if (placeholder != null) {
                        disposable?.add(
                                workOnMainThread({
                                    setBitmapToImageView(placeholder, imageView)
                                }, onError)
                        )
                    }
                }

                disposable?.add(
                        workOnMainThread({
                            spectreOptions.loadingFromNetwork?.invoke(true)
                        }, onError)
                )

                if (spectreOptions.progressListener != null) {

                    disposable?.add(progressSubject
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ progressPercent ->

                                spectreOptions.progressListener?.invoke(progressPercent)

                            }, { e -> onError?.invoke() }))
                }

                bitmap = getBitMapFromNetwork(spectreOptions.imageUrl)
            }

            if (bitmap != null) {

                bitmap = bitmap.circleCentreCrop(imageView.measuredWidth, imageView.measuredHeight)

                memoryLruCache.saveBitmapToCache(spectreOptions.imageUrl, bitmap)
                diskLruCache.saveBitmapToCache(spectreOptions.imageUrl, bitmap)

                disposable?.add(
                        workOnMainThread({
                            setBitmapToImageView(bitmap, imageView)
                        }, onError)
                )
            }
        }, onError))
    }

    private fun getBitMapFromNetwork(imageUrl: String): Bitmap? {

        var bitmap: Bitmap? = null

        val request = Request.Builder().url(imageUrl).build()
        val response = okHttpClient.newCall(request).execute()
        val inputStream = response.body()?.byteStream()

        if (inputStream != null) {
            bitmap = BitmapFactory.decodeStream(inputStream)
        }

        return bitmap
    }

    private fun setBitmapToImageView(originalBitmap: Bitmap, imageView: ImageView) {

        val bitmap = originalBitmap.circleCentreCrop()

        //Freeing up the old bitmaps
        val drawable = imageView.drawable
        if (drawable is BitmapDrawable) {
            val oldBitmap = drawable.bitmap
            oldBitmap?.recycle()
        }

        imageView.setImageBitmap(bitmap)
    }
}