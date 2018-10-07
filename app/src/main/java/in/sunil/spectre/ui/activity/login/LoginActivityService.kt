package `in`.sunil.spectre.ui.activity.login

import `in`.sunil.spectre.BuildConfig
import `in`.sunil.spectre.R
import `in`.sunil.spectre.databinding.ActivityLoginBinding
import `in`.sunil.spectre.ui.activity.search.SearchActivity
import android.app.Activity
import android.net.Uri
import android.support.design.widget.Snackbar
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse

/**
 * Created by Sunil on 10/6/18.
 */

class LoginActivityService : ILoginActivityService {

    private val activity: Activity
    private val loginBinding: ActivityLoginBinding

    constructor(activity: Activity, loginBinding: ActivityLoginBinding) {

        this.activity = activity
        this.loginBinding = loginBinding
    }

    override fun makeSpotifyLoginRequest() {

        val uri = Uri.Builder()
                .scheme(activity.getString(R.string.com_spotify_sdk_redirect_scheme))
                .authority(activity.getString(R.string.com_spotify_sdk_redirect_host))
                .build()

        val request = AuthenticationRequest.Builder(BuildConfig.SPOTIFY_CLIENT_ID,
                AuthenticationResponse.Type.TOKEN, uri.toString())
                .setShowDialog(false)
                .build()

        AuthenticationClient.openLoginActivity(activity, LoginActivityViewModel.AUTH_TOKEN_REQUEST_CODE, request)
    }

    override fun openSearch() {

        SearchActivity.launch(activity)
        activity.finish()
    }

    override fun showLoginFailed() {

        Snackbar.make(loginBinding.root, activity.getString(R.string.login_error), Snackbar.LENGTH_SHORT).show()
    }

}