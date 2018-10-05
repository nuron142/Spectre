package `in`.sunil.spectre.ui.activity.search

import `in`.sunil.spectre.databinding.ActivitySearchBinding
import `in`.sunil.spectre.ui.activity.ArtistDetailActivity
import android.content.Context
import android.support.design.widget.Snackbar

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

    override fun openTrackPage(artistId: String) {

        ArtistDetailActivity.launch(context, artistId)
    }

    override fun showTrackPageError() {

        Snackbar.make(searchActivityBinding.root, "Track details is not implemented",
                Snackbar.LENGTH_SHORT).show()
    }

}