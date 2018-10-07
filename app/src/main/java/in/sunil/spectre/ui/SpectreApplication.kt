package `in`.sunil.spectre.ui

import `in`.sunil.spectre.di.AppComponent
import `in`.sunil.spectre.di.AppModule
import `in`.sunil.spectre.di.DaggerAppComponent
import `in`.sunil.spectre.ui.receiver.NetworkChangeReceiver
import android.app.Application
import android.content.IntentFilter
import android.net.ConnectivityManager
import io.reactivex.plugins.RxJavaPlugins

/**
 * Created by Sunil on 10/1/18.
 */
class SpectreApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        setUpDaggerModule()

        RxJavaPlugins.setErrorHandler { e -> }

        registerReceiver(NetworkChangeReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    private fun setUpDaggerModule() {

        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }
}