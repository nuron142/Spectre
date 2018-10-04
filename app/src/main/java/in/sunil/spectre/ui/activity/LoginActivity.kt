package `in`.sunil.spectre.ui.activity

import `in`.sunil.spectre.R
import `in`.sunil.spectre.databinding.ActivityLoginBinding
import `in`.sunil.spectre.network.NetworkService
import `in`.sunil.spectre.ui.SpectreApplication
import `in`.sunil.spectre.util.delayOnMainThread
import `in`.sunil.spectre.util.isNotEmpty
import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit
import javax.inject.Inject


/**
 * Created by Sunil on 26/09/17.
 */

class LoginActivity : AppCompatActivity() {

    companion object {

        val TAG = ArtistDetailActivity::class.java.simpleName

        fun launch(activity: Activity) {

            val intent = Intent(activity, LoginActivity::class.java)
            activity.startActivity(intent)
        }
    }

    @Inject
    lateinit var networkService: NetworkService

    private lateinit var binding: ActivityLoginBinding

    private var disposable = CompositeDisposable()

    private val CLIENT_ID = "edfa71c724a742f98c43a2e06a06309d"
    private val CLIENT_SECRET = "cee1823284924b77a20639a1483587b5"
    private val REDIRECT_URI = "spectre://callback"
    private val spotifyAppRemote: SpotifyAppRemote? = null

    val AUTH_TOKEN_REQUEST_CODE = 0x10
    val AUTH_CODE_REQUEST_CODE = 0x11
    private var accessToken: String? = null
    private var accessCode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as SpectreApplication).appComponent.inject(this)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        binding.login.setOnClickListener {

            onRequestTokenClicked()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }


    fun onRequestCodeClicked() {
        val request = getAuthenticationRequest(AuthenticationResponse.Type.CODE)
        AuthenticationClient.openLoginActivity(this, AUTH_CODE_REQUEST_CODE, request)
    }

    private fun onRequestTokenClicked() {

        val request = getAuthenticationRequest(AuthenticationResponse.Type.TOKEN)
        AuthenticationClient.openLoginActivity(this, AUTH_TOKEN_REQUEST_CODE, request)
    }

    private fun getAuthenticationRequest(type: AuthenticationResponse.Type): AuthenticationRequest {
        return AuthenticationRequest.Builder(CLIENT_ID, type, getRedirectUri().toString())
                .setShowDialog(false)
                .setScopes(arrayOf("user-read-email"))
                .build()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val response = AuthenticationClient.getResponse(resultCode, data)

        if (AUTH_TOKEN_REQUEST_CODE == requestCode) {
            accessToken = response.accessToken

        } else if (AUTH_CODE_REQUEST_CODE == requestCode) {
            accessCode = response.code
        }

        Log.d(TAG, "Testing4 : accessToken : $accessToken, accessCode: $accessCode")

        networkService.setAccessToken(accessToken)

        handleLoginCallback(accessToken)
    }


    private fun handleLoginCallback(accessToken: String?) {

        if (accessToken.isNotEmpty()) {
            Snackbar.make(binding.root, "Login Successful", Snackbar.LENGTH_SHORT).show()

            delayOnMainThread({
                SearchActivity.launch(this)
            }, 800, TimeUnit.MILLISECONDS)

        } else {

            Snackbar.make(binding.root, "Login Failed. Please try again", Snackbar.LENGTH_SHORT).show()
        }
    }


    private fun getRedirectUri(): Uri {
        return Uri.Builder()
                .scheme(getString(R.string.com_spotify_sdk_redirect_scheme))
                .authority(getString(R.string.com_spotify_sdk_redirect_host))
                .build()
    }
}
