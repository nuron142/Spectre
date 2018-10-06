package `in`.sunil.spectre.ui.activity.search.viewmodels

import `in`.sunil.spectre.network.api.search.Album
import `in`.sunil.spectre.ui.adapter.ViewModel
import android.databinding.ObservableField
import java.text.SimpleDateFormat

/**
 * Created by Sunil on 10/5/18.
 */
class SearchAlbumViewModel : ViewModel {

    val name = ObservableField<String>("")

    val albumName = ObservableField<String>("")
    val albumArtists = ObservableField<String>("")
    val albumReleaseDate = ObservableField<String>("")
    val albumImageUrl = ObservableField<String>("")

    private var onClickAction: ((String) -> Unit)?

    private val album: Album

    constructor(album: Album, onClickAction: ((String) -> Unit)? = null) {

        this.album = album
        this.onClickAction = onClickAction

        setUpViewModel()
    }

    private fun setUpViewModel() {

        album.apply {

            albumName.set(name?.capitalize())

            albumArtists.set(artists?.firstOrNull()?.name)

            releaseDate?.let { dateString -> albumReleaseDate.set(getFormattedReleaseDate(dateString)) }

            albumImageUrl.set(images?.firstOrNull()?.url)
        }

    }

    private fun getFormattedReleaseDate(dateString: String): String {

        try {

            val pattern = when (album.releaseDatePrecision) {
                "day" -> "yyyy-MM-dd"
                "month" -> "yyyy-MM"
                else -> "yyyy"
            }

            val format = SimpleDateFormat(pattern)
            val date = format.parse(dateString)

            val newFormat = SimpleDateFormat("MMM yyyy")
            return newFormat.format(date)

        } catch (t: Throwable) {
            return ""
        }
    }

    fun onClick() = {

        album.artists?.firstOrNull()?.id?.let { id -> onClickAction?.invoke(id) }
    }
}