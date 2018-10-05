package `in`.sunil.spectre.util

import android.databinding.ObservableField
import com.google.gson.Gson
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
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


fun <T> ObservableField<T>.toFlowable(): Flowable<T> {

    return Flowable.create({ emitter ->

        val field = this
        val value = field.get()
        if (value != null) {
            emitter.onNext(value)
        }

        val callback = object : android.databinding.Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(observable: android.databinding.Observable, i: Int) {

                val newValue = field.get()
                if (newValue != null) {
                    emitter.onNext(newValue)
                }
            }
        }

        field.addOnPropertyChangedCallback(callback)
        emitter.setDisposable(Disposables.fromAction { field.removeOnPropertyChangedCallback(callback) })

    }, BackpressureStrategy.BUFFER)
}