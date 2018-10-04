package `in`.sunil.spectre.network.api.search

import com.google.gson.annotations.SerializedName

/**
 * Created by Sunil on 10/5/18.
 */
class SearchResponse {

    @SerializedName("albums")
    val albums: AlbumList? = null

    @SerializedName("tracks")
    val tracks: TrackList? = null

}