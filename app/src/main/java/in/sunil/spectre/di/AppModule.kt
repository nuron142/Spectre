package `in`.sunil.spectre.di

import `in`.sunil.spectre.spectre.Spectre
import `in`.sunil.spectre.ui.SpectreApplication
import android.content.Context
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Singleton

/**
 * Created by Sunil on 10/1/18.
 */

@Module
class AppModule {

    private val spectreApplication: SpectreApplication

    constructor(spectreApplication: SpectreApplication){
        this.spectreApplication = spectreApplication
    }


    @Provides
    @Singleton
    fun provideContext(): Context {
        return spectreApplication
    }

    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient {
        return OkHttpClient.Builder()
                .build()
    }

    @Provides
    @Singleton
    fun provideSpectre(context: Context, okHttpClient: OkHttpClient): Spectre {
        return Spectre(context, okHttpClient)
    }
}