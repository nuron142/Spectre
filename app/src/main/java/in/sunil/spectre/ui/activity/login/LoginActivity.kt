package `in`.sunil.spectre.ui.activity.login

import `in`.sunil.spectre.R
import `in`.sunil.spectre.databinding.ActivityLoginBinding
import `in`.sunil.spectre.ui.SpectreApplication
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity


/**
 * Created by Sunil on 26/09/17.
 */

class LoginActivity : AppCompatActivity() {

    companion object {

        val TAG = LoginActivity::class.java.simpleName
    }

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginActivityViewModel: LoginActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        loginActivityViewModel = LoginActivityViewModel(LoginActivityService(this, binding))
        (application as SpectreApplication).appComponent.inject(loginActivityViewModel)

        binding.vm = loginActivityViewModel
        binding.executePendingBindings()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        loginActivityViewModel.handleActivityResult(requestCode, resultCode, data)

    }
}
