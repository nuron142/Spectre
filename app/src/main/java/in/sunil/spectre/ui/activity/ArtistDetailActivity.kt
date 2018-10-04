package `in`.sunil.spectre.ui.activity

import `in`.sunil.spectre.R
import `in`.sunil.spectre.databinding.ActivityArtistBinding
import `in`.sunil.spectre.network.NetworkService
import `in`.sunil.spectre.ui.SpectreApplication
import `in`.sunil.spectre.util.getJson
import `in`.sunil.spectre.util.isNotEmpty
import `in`.sunil.spectre.util.workOnBackgroundThread
import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

/**
 * Created by Sunil on 10/4/18.
 */
class ArtistDetailActivity : AppCompatActivity() {

    companion object {

        val TAG = ArtistDetailActivity::class.java.simpleName

        fun launch(activity: Activity) {

            val intent = Intent(activity, ArtistDetailActivity::class.java)
            activity.startActivity(intent)
        }
    }

    @Inject
    lateinit var networkService: NetworkService

    private lateinit var binding: ActivityArtistBinding

    private var disposable = CompositeDisposable()
    private var artistDetailDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as SpectreApplication).appComponent.inject(this)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_artist)

        getArtistDetail("7n2Ycct7Beij7Dj7meI4X0")
    }

    fun getArtistDetail(artistID: String?) {

        if (artistID?.isNotEmpty() == true) {

            artistDetailDisposable = workOnBackgroundThread({

                val artistDetailResponse = networkService.getArtistDetail(artistID)
                Log.d(TAG, "Testing4 : " + artistDetailResponse?.getJson())
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}
