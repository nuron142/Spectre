package `in`.sunil.spectre.ui.activity.search.viewmodels

import `in`.sunil.spectre.network.api.search.Track
import `in`.sunil.spectre.ui.adapter.ViewModel
import android.databinding.ObservableField

/**
 * Created by Sunil on 10/5/18.
 */
class SearchTrackViewModel : ViewModel {

    val albumName = ObservableField<String>("")
    val albumArtists = ObservableField<String>("")
    val trackName = ObservableField<String>("")
    val albumImageUrl = ObservableField<String>("")

    val name = ObservableField<String>("")
    private var onClickAction: (String) -> Unit

    private val track: Track

    constructor(track: Track, onClickAction: (String) -> Unit) {

        this.track = track
        this.onClickAction = onClickAction

        setUpViewModel()
    }

    private fun setUpViewModel() {

        trackName.set(track.name)

        track.album?.apply {

            albumName.set(name)

            albumArtists.set(artists?.firstOrNull()?.name)

            albumImageUrl.set(images?.firstOrNull()?.url)
        }

    }

    fun onClick() = {

        track.album?.artists?.firstOrNull()?.id?.let { id -> onClickAction.invoke(id) }
    }
}