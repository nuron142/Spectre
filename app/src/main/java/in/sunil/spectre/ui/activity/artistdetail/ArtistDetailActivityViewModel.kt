package `in`.sunil.spectre.ui.activity.artistdetail

import `in`.sunil.spectre.network.NetworkService
import `in`.sunil.spectre.network.api.artist.ArtistDetailResponse
import `in`.sunil.spectre.util.getJson
import `in`.sunil.spectre.util.isNotEmpty
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Sunil on 10/5/18.
 */

class ArtistDetailActivityViewModel {

    companion object {

        val TAG: String = ArtistDetailActivityViewModel::class.java.simpleName

    }

    @Inject
    lateinit var networkService: NetworkService

    private var artistId: String? = null
    private var artistDetailActivityService: IArtistDetailActivityService
    private var disposable = CompositeDisposable()
    private var artistDetailDisposable: Disposable? = null

    val showProgress = ObservableBoolean(true)

    val artistName = ObservableField<String>("")
    val genres = ObservableField<String>("")
    val albumReleaseDate = ObservableField<String>("")
    val artistImageUrl = ObservableField<String>("")

    constructor(artistId: String?, artistDetailActivityService: IArtistDetailActivityService) {

        this.artistId = artistId
        this.artistDetailActivityService = artistDetailActivityService
    }

    fun init() {

        setUpViewModel()
    }

    private fun setUpViewModel() {

        artistId?.let { id -> getArtistDetail(id) }
    }

    private fun getArtistDetail(artistId: String) {

        artistDetailDisposable?.dispose()

        if (artistId.isNotEmpty()) {

            showProgress.set(true)

            artistDetailDisposable = networkService.getArtistDetailFlowable(artistId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ artistDetailResponse ->

                        Log.d(TAG, "Testing4 : " + artistDetailResponse.getJson())
                        handleArtistDetailResponse(artistDetailResponse)

                    }, { e -> Log.e(TAG, "Error Testing4 : $e") })

            artistDetailDisposable?.let { disposable.add(it) }

        } else {

            showProgress.set(false)
        }
    }

    private fun handleArtistDetailResponse(artistDetailResponse: ArtistDetailResponse) {

        showProgress.set(false)

        artistName.set(artistDetailResponse.name)
        artistImageUrl.set(artistDetailResponse.images?.firstOrNull()?.url)

        val genreString = StringBuilder()

        val genresSize = (artistDetailResponse.genres?.size ?: 0) - 1
        artistDetailResponse.genres?.forEachIndexed { index, genre ->
            genreString.append(genre.capitalize())
            if (index != genresSize) {
                genreString.append(", ")
            }
        }

        genres.set(genreString.toString())
    }

    fun onCloseButtonClick() = {

        artistDetailActivityService.closeArtistDetail()
    }

    fun onDestroy() {

        disposable.dispose()
    }
}
