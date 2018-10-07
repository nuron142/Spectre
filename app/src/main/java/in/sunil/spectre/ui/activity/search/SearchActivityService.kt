package `in`.sunil.spectre.ui.activity.search

import `in`.sunil.spectre.R
import `in`.sunil.spectre.databinding.ActivitySearchBinding
import `in`.sunil.spectre.ui.activity.artistdetail.ArtistDetailActivity
import android.app.Activity
import android.support.design.widget.Snackbar

/**
 * Created by Sunil on 10/6/18.
 */
class SearchActivityService : ISearchActivityService {

    private val activity: Activity
    private val searchActivityBinding: ActivitySearchBinding

    constructor(activity: Activity, searchActivityBinding: ActivitySearchBinding) {

        this.activity = activity
        this.searchActivityBinding = searchActivityBinding
    }

    override fun openArtistDetailPage(artistId: String) {

        ArtistDetailActivity.launch(activity, artistId)
    }

    override fun showError() {

        Snackbar.make(searchActivityBinding.root, activity.getString(R.string.general_error), Snackbar.LENGTH_SHORT).show()
    }

    override fun showNoResultsFound() {

        Snackbar.make(searchActivityBinding.root, activity.getString(R.string.no_results_found), Snackbar.LENGTH_SHORT).show()
    }
}