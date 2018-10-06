package `in`.sunil.spectre.ui.activity

import `in`.sunil.spectre.BuildConfig
import `in`.sunil.spectre.R
import `in`.sunil.spectre.databinding.ActivityLoginBinding
import `in`.sunil.spectre.network.NetworkService
import `in`.sunil.spectre.ui.SpectreApplication
import `in`.sunil.spectre.ui.activity.search.SearchActivity
import `in`.sunil.spectre.util.delayOnMainThread
import `in`.sunil.spectre.util.isNotEmpty
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import java.util.concurrent.TimeUnit
import javax.inject.Inject


/**
 * Created by Sunil on 26/09/17.
 */

class LoginActivity : AppCompatActivity() {

    companion object {

        val TAG = LoginActivity::class.java.simpleName

        private const val AUTH_TOKEN_REQUEST_CODE = 142
    }

    @Inject
    lateinit var networkService: NetworkService

    private lateinit var binding: ActivityLoginBinding

    private var accessToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as SpectreApplication).appComponent.inject(this)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        binding.login.setOnClickListener {
            onLoginClick()
        }
    }

    private fun onLoginClick() {

        val request = getAuthenticationRequest(AuthenticationResponse.Type.TOKEN)
        AuthenticationClient.openLoginActivity(this, AUTH_TOKEN_REQUEST_CODE, request)
    }

    private fun getAuthenticationRequest(type: AuthenticationResponse.Type): AuthenticationRequest {

        val uri = Uri.Builder()
                .scheme(getString(R.string.com_spotify_sdk_redirect_scheme))
                .authority(getString(R.string.com_spotify_sdk_redirect_host))
                .build()

        return AuthenticationRequest.Builder(BuildConfig.SPOTIFY_CLIENT_ID, type, uri.toString())
                .setShowDialog(false)
                .build()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val response = AuthenticationClient.getResponse(resultCode, data)

        if (AUTH_TOKEN_REQUEST_CODE == requestCode) {
            accessToken = response.accessToken
        }

        networkService.setAccessToken(accessToken)

        handleLoginCallback(accessToken)
    }


    private fun handleLoginCallback(accessToken: String?) {

        if (accessToken.isNotEmpty()) {

            delayOnMainThread({

                SearchActivity.launch(this)
                finish()

            }, 500, TimeUnit.MILLISECONDS)

        } else {

            Snackbar.make(binding.root, "Login Failed. Please try again", Snackbar.LENGTH_SHORT).show()
        }
    }
}
