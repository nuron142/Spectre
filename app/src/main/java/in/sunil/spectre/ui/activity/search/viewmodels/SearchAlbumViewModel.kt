package `in`.sunil.spectre.ui.activity.search.viewmodels

import `in`.sunil.spectre.network.api.search.Album
import `in`.sunil.spectre.ui.activity.search.SearchActivityViewModel
import `in`.sunil.spectre.ui.adapter.ViewModel
import android.databinding.ObservableField

/**
 * Created by Sunil on 10/5/18.
 */
class SearchAlbumViewModel : ViewModel {

    val name = ObservableField<String>("")

    val albumName = ObservableField<String>("")
    val albumArtists = ObservableField<String>("")
    val albumReleaseDate = ObservableField<String>("")
    val albumImageUrl = ObservableField<String>("")

    private var onClickAction: (String) -> Unit

    private val album: Album

    constructor(album: Album, onClickAction: (String) -> Unit) {

        this.album = album
        this.onClickAction = onClickAction

        setUpViewModel()
    }

    private fun setUpViewModel() {

        album.apply {

            albumName.set(name)

            albumArtists.set(artists?.firstOrNull()?.name)
            albumReleaseDate.set(releaseDate)

            albumImageUrl.set(images?.firstOrNull()?.url)
        }

    }

    fun onClick() = {

        album.artists?.firstOrNull()?.id?.let { id -> onClickAction.invoke(id) }
    }

    override fun getType(): Int {

        return SearchActivityViewModel.VIEW_TYPE_ARTIST
    }
}