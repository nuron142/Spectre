package `in`.sunil.spectre.network

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*

import java.io.IOException

/**
 * Created by Sunil on 27/09/17.
 */

class OkHttpDownloadProgressResponseBody : ResponseBody {

    private val responseUrl: String
    private val responseBody: ResponseBody?
    private val okHttpDownloadProgressListener: OkHttpDownloadProgressListener

    constructor(responseUrl: String, responseBody: ResponseBody?,
                okHttpDownloadProgressListener: OkHttpDownloadProgressListener) : super() {

        this.responseUrl = responseUrl
        this.responseBody = responseBody
        this.okHttpDownloadProgressListener = okHttpDownloadProgressListener
    }

    private var bufferedSource: BufferedSource? = null

    override fun contentType(): MediaType? {
        return responseBody?.contentType()
    }

    override fun contentLength(): Long {
        return responseBody?.contentLength() ?: -1L
    }

    override fun source(): BufferedSource? {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody?.source()))
        }
        return bufferedSource
    }

    private fun source(source: Source?): Source {

        return object : ForwardingSource(source) {

            var totalBytesRead = 0L

            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {

                val bytesRead = super.read(sink, byteCount)
                totalBytesRead += if (bytesRead != -1L) bytesRead else 0

                val contentLength = responseBody?.contentLength() ?: -1

                if (contentLength != -1L) {
                    //progress value is in percent
                    okHttpDownloadProgressListener.progress(responseUrl, ((100 * totalBytesRead) / contentLength).toInt(), bytesRead == -1L)
                }

                return bytesRead
            }
        }
    }
}
