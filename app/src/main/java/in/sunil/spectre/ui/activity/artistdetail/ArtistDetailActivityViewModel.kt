package `in`.sunil.spectre.ui.activity.artistdetail

import `in`.sunil.spectre.R
import `in`.sunil.spectre.network.INetworkService
import `in`.sunil.spectre.network.api.artist.ArtistDetailResponse
import `in`.sunil.spectre.network.api.toptracks.ArtistTopAlbumsResponse
import `in`.sunil.spectre.ui.activity.search.SearchActivityViewModel
import `in`.sunil.spectre.ui.activity.search.viewmodels.SearchAlbumViewModel
import `in`.sunil.spectre.ui.adapter.ViewModel
import android.databinding.ObservableArrayList
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
    lateinit var networkService: INetworkService

    private var artistId: String? = null
    private var artistDetailActivityService: IArtistDetailActivityService
    private var disposable = CompositeDisposable()
    private var artistDetailDisposable: Disposable? = null

    val showProgress = ObservableBoolean(true)

    val artistName = ObservableField<String>("")
    val genres = ObservableField<String>("")
    val albumReleaseDate = ObservableField<String>("")
    val artistImageUrl = ObservableField<String>("")

    val showTopTracks = ObservableBoolean(false)

    var dataSet = ObservableArrayList<ViewModel>()
    val viewModelLayoutIdMap: HashMap<Class<out ViewModel>, Int> = hashMapOf(

            SearchAlbumViewModel::class.java to R.layout.item_artist_top_album
    )

    private var shouldRetryApiCall = false

    constructor(artistId: String?, artistDetailActivityService: IArtistDetailActivityService) {

        this.artistId = artistId
        this.artistDetailActivityService = artistDetailActivityService
    }

    fun init() {

        setUpViewModel()
    }

    private fun setUpViewModel() {

        getArtistDetail(artistId)

        disposable.add(networkService.subscribeNetworkChangeSubject()
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ networkAvailable ->

                    if (networkAvailable && shouldRetryApiCall) {
                        shouldRetryApiCall = false
                        getArtistDetail(artistId)
                    }

                }, { e -> Log.d(SearchActivityViewModel.TAG, "" + e.message) }))

    }

    private fun getArtistDetail(artistId: String?) {

        artistDetailDisposable?.dispose()

        if (artistId != null && !artistId.isEmpty()) {

            showProgress.set(true)

            artistDetailDisposable = networkService.getArtistDetailFlowable(artistId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ artistDetailResponse ->

                        handleArtistDetailResponse(artistId, artistDetailResponse)

                    }, { e ->

                        Log.e(TAG, "Error : $e")
                        handleArtistDetailFailed()
                    })

            artistDetailDisposable?.let { disposable.add(it) }

        } else {

            showProgress.set(false)
        }
    }

    private fun handleArtistDetailResponse(artistId: String, artistDetailResponse: ArtistDetailResponse) {

        showProgress.set(false)

        dataSet.clear()

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

        getArtistTopTracks(artistId)
    }

    private fun handleArtistDetailFailed() {

        showProgress.set(false)

        shouldRetryApiCall = true
        artistDetailActivityService.showError()
    }

    private fun getArtistTopTracks(artistId: String) {

        disposable.add(networkService.getArtistTopAlbumsFlowable(artistId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ artistTopAlbumsResponse ->

                    handleArtistTopTracksResponse(artistTopAlbumsResponse)

                }, { e ->

                    Log.e(TAG, "Error : $e")
                    artistDetailActivityService.showError()
                }))

    }


    private fun handleArtistTopTracksResponse(artistTopAlbumsResponse: ArtistTopAlbumsResponse?) {

        artistTopAlbumsResponse?.apply {

            dataSet.clear()

            showTopTracks.set(items?.isNotEmpty() == true)

            items?.forEach { album ->

                if (album.name != null) {
                    val searchArtistViewModel = SearchAlbumViewModel(album)
                    dataSet.add(searchArtistViewModel)
                }
            }
        }
    }


    fun onCloseButtonClick() = {

        artistDetailActivityService.closeArtistDetail()
    }

    fun onDestroy() {

        disposable.dispose()
    }
}
