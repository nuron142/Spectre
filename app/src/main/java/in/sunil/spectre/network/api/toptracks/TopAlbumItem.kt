package `in`.sunil.spectre.network.api.toptracks

import `in`.sunil.spectre.network.api.search.Artist
import `in`.sunil.spectre.network.api.search.SpotifyImage
import com.google.gson.annotations.SerializedName

/**
 * Created by Sunil on 10/5/18.
 */
class TopAlbumItem {

    @SerializedName("album_type")
    val albumType: String? = null

    @SerializedName("artists")
    val artists: List<Artist>? = null

    @SerializedName("id")
    val id: String? = null

    @SerializedName("images")
    val images: List<SpotifyImage>? = null

    @SerializedName("name")
    val name: String? = null

    @SerializedName("release_date")
    val releaseDate: String? = null

    @SerializedName("total_tracks")
    val totalTracks: String? = null

    @SerializedName("type")
    val type: String? = null
}