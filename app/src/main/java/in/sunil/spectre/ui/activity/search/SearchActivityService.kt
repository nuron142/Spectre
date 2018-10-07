package `in`.sunil.spectre.ui.activity.search

import `in`.sunil.spectre.databinding.ActivitySearchBinding
import `in`.sunil.spectre.ui.activity.artistdetail.ArtistDetailActivity
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

    override fun openArtistDetailPage(artistId: String) {

        ArtistDetailActivity.launch(context, artistId)
    }

    override fun showError() {

        Snackbar.make(searchActivityBinding.root, "Something went wrong. Please try again", Snackbar.LENGTH_SHORT).show()
    }

    override fun showNoResultsFound() {

        Snackbar.make(searchActivityBinding.root, "No results found", Snackbar.LENGTH_SHORT).show()
    }
}