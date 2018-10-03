package `in`.sunil.spectre.network

/**
 * Created by Sunil on 27/09/17.
 */

interface OkHttpDownloadProgressListener {

    fun progress(url: String, progress: Int, complete: Boolean)
}