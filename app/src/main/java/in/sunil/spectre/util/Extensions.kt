package `in`.sunil.spectre.util

import android.graphics.*
import android.media.ThumbnailUtils
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Callable


/**
 * Created by Sunil on 26/09/17.
 */

fun workOnMainThread(block: () -> Unit, onError: (() -> Unit)? = null): Disposable {

    return RxUtils.completable(Callable {
        block.invoke()
        true
    }, onError, AndroidSchedulers.mainThread())
}

fun workOnBackgroundThread(block: () -> Unit, onError: (() -> Unit)? = null): Disposable {

    return RxUtils.completable(Callable {
        block.invoke()
        true
    }, onError, Schedulers.io())
}

fun Bitmap.circleCentreCrop(width: Int = Int.MAX_VALUE, height: Int = Int.MAX_VALUE): Bitmap {

    val minWidth = if (width > 0) Math.min(this.width, width) else this.width
    val minHeight = if (height > 0) Math.min(this.height, height) else this.height

    val size = Math.min(minWidth, minHeight)

    val bitmap = ThumbnailUtils.extractThumbnail(this, size, size)

    val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(output)

    val paint = Paint()
    val rect = Rect(0, 0, bitmap.width, bitmap.height)

    paint.isAntiAlias = true
    canvas.drawARGB(0, 0, 0, 0)
    paint.color = 0xff424242.toInt()
    canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint)
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

    canvas.drawBitmap(bitmap, rect, rect, paint)

    return output
}

fun Any.getJson(): String? {
    return Gson().toJson(this)
}
