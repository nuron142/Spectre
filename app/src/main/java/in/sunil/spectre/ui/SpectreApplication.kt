package `in`.sunil.spectre.ui

import `in`.sunil.spectre.di.AppComponent
import `in`.sunil.spectre.di.AppModule
import `in`.sunil.spectre.di.DaggerAppComponent
import android.app.Application
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
    }

    private fun setUpDaggerModule() {

        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }
}