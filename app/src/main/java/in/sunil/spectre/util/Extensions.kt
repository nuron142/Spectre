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

fun Any.getJson(): String? {
    return Gson().toJson(this)
}
