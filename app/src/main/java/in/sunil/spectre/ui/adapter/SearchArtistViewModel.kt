package `in`.sunil.spectre.ui.adapter

import `in`.sunil.spectre.network.api.search.Album
import `in`.sunil.spectre.ui.adapter.SearchAdapter.Companion.VIEW_TYPE_ARTIST
import android.databinding.ObservableField

/**
 * Created by Sunil on 10/5/18.
 */
class SearchArtistViewModel : ViewModel {

    val name = ObservableField<String>("")
    private var onClickAction: (String) -> Unit

    private val album: Album

    constructor(album: Album, onClickAction: (String) -> Unit) {

        this.album = album
        this.onClickAction = onClickAction

        this.name.set(album.name)
    }

    fun onClick() = {

        album.artists?.firstOrNull()?.id?.let { id -> onClickAction.invoke(id) }
    }

    override fun getType(): Int {
        return VIEW_TYPE_ARTIST
    }
}