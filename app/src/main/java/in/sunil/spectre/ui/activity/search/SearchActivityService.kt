package `in`.sunil.spectre.ui.activity.search

import `in`.sunil.spectre.databinding.ActivitySearchBinding
import `in`.sunil.spectre.ui.activity.artistdetail.ArtistDetailActivity
import android.content.Context

/**
 * Created by Sunil on 10/6/18.
 */
class SearchActivityService : ISearchActivityService {

    private val context: Context
    private val searchActivityBinding: ActivitySearchBinding

    constructor(context: Context, searchActivityBinding: ActivitySearchBinding) {

        this.context = context
        this.searchActivityBinding = searchActivityBinding
    }

    override fun openArtistDetailPage(artistId: String) {

        ArtistDetailActivity.launch(context, artistId)
    }

}