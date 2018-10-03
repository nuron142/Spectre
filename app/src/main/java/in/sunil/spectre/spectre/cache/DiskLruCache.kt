package `in`.sunil.spectre.spectre.cache

import `in`.sunil.spectre.util.getJson
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.preference.PreferenceManager
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileOutputStream

/**
 * Created by Sunil on 9/29/18.
 */

class DiskLruCache : LruCache<String, String> {

    private val TAG = DiskLruCache::class.java.simpleName
    private val SPECTRE_DISK_CACHE_DIR = "spectreDiskCacheDir"
    private val SPECTRE_DISK_CACHE_ENTRIES = "spectreDiskCacheEntries"

    private val baseFileDir: File

    private val sharedPreferences: SharedPreferences

    constructor(context: Context, maxCacheEntries: Int, maxCacheSize: Long) : super(maxCacheEntries, maxCacheSize) {

        baseFileDir = context.filesDir
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        initialiseCache()
    }

    /*
        Initialise the disk cache entries with the images which are saved in cache dir.
        The cache entries are saved as json in shared preferences for faster access
     */
    private fun initialiseCache() {

        val cacheEntries: List<Node<String, String>>? = Gson().fromJson(sharedPreferences.getString(SPECTRE_DISK_CACHE_ENTRIES, null), object : TypeToken<List<Node<String, String>>>() {}.type)

        cacheEntries?.forEach { node ->

            val key = node.key
            val value = node.value
            if (key != null && value != null) {
                set(key, value, node.size)
            }
        }
    }

    fun getBitmapFromDisk(imageUrl: String): Bitmap? {

        var bitmap: Bitmap? = null

        if (get(imageUrl)?.isNotEmpty() == true) {

            try {

                val file = File(getInternalFolder(SPECTRE_DISK_CACHE_DIR), imageUrl.hashCode().toString())
                bitmap = BitmapFactory.decodeFile(file.path)

            } catch (t: Throwable) {
                Log.e(TAG, t.message)
            }
        }

        return bitmap
    }

    fun saveBitmapToCache(imageUrl: String, bitmap: Bitmap) {

        saveToCacheFolder(imageUrl, bitmap)
    }

    override fun entryAddedToCache(key: String?, value: String?) {

        sharedPreferences.edit().putString(SPECTRE_DISK_CACHE_ENTRIES,
                Gson().toJson(getOrderedEntryList(), object : TypeToken<List<Node<String, String>>>() {}.type)).apply()
    }

    override fun entryRemovedFromCache(key: String?, value: String?) {

        key?.let { deleteFromCacheFolder(key) }

        sharedPreferences.edit().putString(SPECTRE_DISK_CACHE_ENTRIES, getOrderedEntryList().getJson()).apply()
    }

    @Synchronized
    private fun deleteFromCacheFolder(imageUrl: String) {

        try {

            val file = File(getInternalFolder(SPECTRE_DISK_CACHE_DIR), imageUrl.hashCode().toString())

            if (file.exists()) {
                file.delete()
            }

        } catch (t: Throwable) {
            Log.e(TAG, "" + t.message)
        }
    }


    @Synchronized
    private fun saveToCacheFolder(imageUrl: String, bitmap: Bitmap) {

        try {

            val file = File(getInternalFolder(SPECTRE_DISK_CACHE_DIR), imageUrl.hashCode().toString())

            var fileSize = file.length()
            if (!file.exists()) {
                val out = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.WEBP, 100, out)

                fileSize = out.channel.size()
                out.close()
            }

            set(imageUrl, imageUrl, fileSize)

        } catch (t: Throwable) {
            Log.e(TAG, "" + t.message)
        }
    }

    private fun getInternalFolder(path: String): File {

        var dir = baseFileDir
        dir = File(dir, path)
        if (!dir.exists()) {
            dir.mkdir()
        }
        return dir
    }

}