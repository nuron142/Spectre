package `in`.sunil.spectre.network.api.toptracks

import `in`.sunil.spectre.network.api.search.Album
import com.google.gson.annotations.SerializedName

/**
 * Created by Sunil on 10/5/18.
 */

class ArtistTopAlbumsResponse {

    @SerializedName("items")
    val items: List<Album>? = null

}