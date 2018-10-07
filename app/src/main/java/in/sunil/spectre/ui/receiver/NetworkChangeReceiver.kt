package `in`.sunil.spectre.ui.receiver

import `in`.sunil.spectre.network.INetworkService
import `in`.sunil.spectre.ui.SpectreApplication
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import javax.inject.Inject

/**
 * Created by Sunil on 10/7/18.
 */

class NetworkChangeReceiver : BroadcastReceiver() {

    companion object {

        private val TAG = NetworkChangeReceiver::class.java.simpleName
    }

    @Inject
    lateinit var networkService: INetworkService

    override fun onReceive(context: Context, intent: Intent) {

        (context.applicationContext as SpectreApplication).appComponent.inject(this)

        networkService.setNetworkChanged()

    }
}
