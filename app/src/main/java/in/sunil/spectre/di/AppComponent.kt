package `in`.sunil.spectre.di

import `in`.sunil.spectre.ui.SpectreApplication
import `in`.sunil.spectre.ui.activity.MainActivity
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Sunil on 10/1/18.
 */
@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(spectreApplication: SpectreApplication)

    fun inject(mainActivity: MainActivity)
}