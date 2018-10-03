package `in`.sunil.spectre.spectre.cache

import android.graphics.Bitmap
import android.support.v4.graphics.BitmapCompat

/**
 * Created by Sunil on 9/29/18.
 */

class MemoryLruCache : LruCache<String, Bitmap> {

    private val TAG = MemoryLruCache::class.java.simpleName

    constructor(maxCacheEntries: Int, maxCacheSize: Long) : super(maxCacheEntries, maxCacheSize)

    fun getBitmapFromMemory(imageUrl: String): Bitmap? {

        return get(imageUrl)
    }

    fun saveBitmapToCache(imageUrl: String, bitmap: Bitmap) {

        set(imageUrl, bitmap, BitmapCompat.getAllocationByteCount(bitmap).toLong())

    }

    override fun entryAddedToCache(key: String?, value: Bitmap?) {

    }

    override fun entryRemovedFromCache(key: String?, value: Bitmap?) {

        if (value?.isRecycled == false) {
            value.recycle()
        }
    }

}