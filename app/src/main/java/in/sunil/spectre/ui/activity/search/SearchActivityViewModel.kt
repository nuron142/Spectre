package `in`.sunil.spectre.ui.activity.search

import `in`.sunil.spectre.network.NetworkService
import `in`.sunil.spectre.network.api.search.SearchResponse
import `in`.sunil.spectre.ui.activity.search.viewmodels.SearchAlbumViewModel
import `in`.sunil.spectre.ui.activity.search.viewmodels.SearchHeaderViewModel
import `in`.sunil.spectre.ui.activity.search.viewmodels.SearchTrackViewModel
import `in`.sunil.spectre.ui.adapter.ViewModel
import `in`.sunil.spectre.util.getJson
import `in`.sunil.spectre.util.isNotEmpty
import `in`.sunil.spectre.util.toFlowable
import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Sunil on 10/5/18.
 */
class SearchActivityViewModel {

    companion object {

        const val VIEW_TYPE_ARTIST = 0
        const val VIEW_TYPE_TRACK = 1
        const val VIEW_TYPE_HEADER = 2
    }

    @Inject
    lateinit var networkService: NetworkService

    var dataSet = ObservableArrayList<ViewModel>()
    private var disposable = CompositeDisposable()
    private var searchDisposable: Disposable? = null

    val showCloseButton = ObservableBoolean(false)
    private val searchActivityService: ISearchActivityService

    var searchQuery = ObservableField<String>("")

    constructor(searchActivityService: ISearchActivityService) {

        this.searchActivityService = searchActivityService

        setUpViewModel()
    }

    private fun setUpViewModel() {

        disposable.add(searchQuery.toFlowable()
                .map { query ->

                    showCloseButton.set(query.isNotEmpty())
                    return@map query
                }
                .debounce(600, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ query ->
                    getSearchList(query)
                }, { e -> Log.d(SearchActivity.TAG, "" + e.message) }))
    }

    private fun getSearchList(query: String) {

        searchDisposable?.dispose()

        if (query.isNotEmpty()) {

            showCloseButton.set(true)

            searchDisposable = networkService.getSearchQueryFlowable(query)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ searchResponse ->

                        Log.d(SearchActivity.TAG, "Testing4 : " + searchResponse?.getJson())
                        handleSearchResponse(searchResponse)

                    }, { e -> Log.e(SearchActivity.TAG, "Error Testing4: $e") })

        } else {

            dataSet.clear()
            showCloseButton.set(false)
        }
    }


    private fun handleSearchResponse(searchResponse: SearchResponse?) {

        searchResponse?.apply {

            dataSet.clear()

            if (albums?.items?.size ?: 0 > 0) {

                val searchHeaderViewModel = SearchHeaderViewModel("ALBUMS")
                dataSet.add(searchHeaderViewModel)
            }

            albums?.items?.forEach { album ->

                if (album.name != null) {
                    val searchArtistViewModel = SearchAlbumViewModel(album) { artistId ->
                        searchActivityService.openArtistDetailPage(artistId)
                    }
                    dataSet.add(searchArtistViewModel)
                }
            }

            if (tracks?.items?.size ?: 0 > 0) {

                val searchHeaderViewModel = SearchHeaderViewModel("TRACKS")
                dataSet.add(searchHeaderViewModel)
            }

            tracks?.items?.forEach { track ->

                if (track.name != null) {

                    val searchArtistViewModel = SearchTrackViewModel(track) { artistId ->
                        searchActivityService.openArtistDetailPage(artistId)
                    }

                    dataSet.add(searchArtistViewModel)
                }
            }
        }
    }

    fun onCancelButtonCLick() = {

        dataSet.clear()

        searchQuery.set("")
        showCloseButton.set(false)
    }

    fun onDestroy() {

        disposable.dispose()
    }
}
