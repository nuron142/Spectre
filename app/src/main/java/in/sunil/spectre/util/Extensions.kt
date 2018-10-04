package `in`.sunil.spectre.util

import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit


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

fun delayOnMainThread(block: () -> Unit, delay: Long, timeUnit: TimeUnit = TimeUnit.MILLISECONDS): Disposable {

    return RxUtils.delayCompletable(Callable {
        block.invoke()
        true
    }, delay, timeUnit, AndroidSchedulers.mainThread())
}

fun Any.getJson(): String? {
    return Gson().toJson(this)
}

fun <T> String.toClassData(classz: Class<T>): T? {
    return Gson().fromJson(this, classz)
}
