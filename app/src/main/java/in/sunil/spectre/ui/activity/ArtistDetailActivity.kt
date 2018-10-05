package `in`.sunil.spectre.ui.activity

import `in`.sunil.spectre.R
import `in`.sunil.spectre.databinding.ActivityArtistBinding
import `in`.sunil.spectre.network.NetworkService
import `in`.sunil.spectre.network.api.artist.ArtistDetailResponse
import `in`.sunil.spectre.ui.SpectreApplication
import `in`.sunil.spectre.util.getJson
import `in`.sunil.spectre.util.isNotEmpty
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Sunil on 10/4/18.
 */
class ArtistDetailActivity : AppCompatActivity() {

    companion object {

        val TAG = ArtistDetailActivity::class.java.simpleName

        val ARTIST_ID = "artistId"

        fun launch(context: Context, artistId: String) {

            val intent = Intent(context, ArtistDetailActivity::class.java)
            intent.putExtra(ARTIST_ID, artistId)
            context.startActivity(intent)
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

        val artistID = intent.getStringExtra(ARTIST_ID)
        getArtistDetail(artistID)
    }

    private fun getArtistDetail(artistID: String?) {

        if (artistID?.isNotEmpty() == true) {

            artistDetailDisposable = networkService.getArtistDetailFlowable(artistID)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ artistDetailResponse ->

                        Log.d(TAG, "Testing4 : " + artistDetailResponse.getJson())
                        handleArtistDetailResponse(artistDetailResponse)

                    }, { e -> Log.e(TAG, "Error Testing4 : $e") })
        }
    }

    private fun handleArtistDetailResponse(artistDetailResponse: ArtistDetailResponse) {

    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}
