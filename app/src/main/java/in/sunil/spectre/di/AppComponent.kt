package `in`.sunil.spectre.di

import `in`.sunil.spectre.ui.SpectreApplication
import `in`.sunil.spectre.ui.activity.artistdetail.ArtistDetailActivity
import `in`.sunil.spectre.ui.activity.login.LoginActivity
import `in`.sunil.spectre.ui.activity.artistdetail.ArtistDetailActivityViewModel
import `in`.sunil.spectre.ui.activity.login.LoginActivityViewModel
import `in`.sunil.spectre.ui.activity.search.SearchActivity
import `in`.sunil.spectre.ui.activity.search.SearchActivityViewModel
import `in`.sunil.spectre.ui.receiver.NetworkChangeReceiver
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Sunil on 10/1/18.
 */

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(spectreApplication: SpectreApplication)

    fun inject(loginActivity: LoginActivity)

    fun inject(searchActivity: SearchActivity)

    fun inject(artistDetailActivity: ArtistDetailActivity)

    fun inject(searchActivityViewModel: SearchActivityViewModel)

    fun inject(artistDetailActivityViewModel: ArtistDetailActivityViewModel)

    fun inject(loginActivityViewModel: LoginActivityViewModel)

    fun inject(networkChangeReceiver: NetworkChangeReceiver)

}