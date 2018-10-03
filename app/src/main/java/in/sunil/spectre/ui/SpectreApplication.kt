package `in`.sunil.spectre.ui

import `in`.sunil.spectre.di.AppComponent
import `in`.sunil.spectre.di.AppModule
import `in`.sunil.spectre.di.DaggerAppComponent
import android.app.Application

/**
 * Created by Sunil on 10/1/18.
 */
class SpectreApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        setUpDaggerModule()
    }

    private fun setUpDaggerModule() {

        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }
}