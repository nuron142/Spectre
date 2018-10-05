package `in`.sunil.spectre.ui.adapter

import `in`.sunil.spectre.network.api.search.Track
import `in`.sunil.spectre.ui.adapter.SearchAdapter.Companion.VIEW_TYPE_TRACK
import android.databinding.ObservableField

/**
 * Created by Sunil on 10/5/18.
 */
class SearchTrackViewModel : ViewModel {

    val name = ObservableField<String>("")
    private var onClickAction: () -> Unit

    private val track: Track

    constructor(track: Track, onClickAction: () -> Unit) {

        this.track = track
        this.onClickAction = onClickAction

        this.name.set(track.name)
    }

    fun onClick() = {

        onClickAction.invoke()
    }

    override fun getType(): Int {
        return VIEW_TYPE_TRACK
    }
}