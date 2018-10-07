package `in`.sunil.spectre.ui.activity.login

import `in`.sunil.spectre.network.INetworkService
import `in`.sunil.spectre.util.delayOnMainThread
import `in`.sunil.spectre.util.isNotEmpty
import android.content.Intent
import com.spotify.sdk.android.authentication.AuthenticationClient
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Sunil on 10/5/18.
 */
class LoginActivityViewModel {

    companion object {

        val TAG = LoginActivityViewModel::class.java.simpleName

        const val AUTH_TOKEN_REQUEST_CODE = 142
    }

    @Inject
    lateinit var networkService: INetworkService

    private val loginActivityService: ILoginActivityService

    constructor(loginActivityService: LoginActivityService) {

        this.loginActivityService = loginActivityService
    }

    fun onLoginClick() = {

        loginActivityService.makeSpotifyLoginRequest()
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        val response = AuthenticationClient.getResponse(resultCode, data)

        if (AUTH_TOKEN_REQUEST_CODE == requestCode) {

            val accessToken: String? = response.accessToken

            networkService.setAccessToken(accessToken)

            handleLoginCallback(accessToken)
        }
    }


    private fun handleLoginCallback(accessToken: String?) {

        if (accessToken.isNotEmpty()) {

            delayOnMainThread({

                loginActivityService.openSearch()

            }, 500, TimeUnit.MILLISECONDS)

        } else {

            loginActivityService.showLoginFailed()
        }
    }
}
