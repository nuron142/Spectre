package `in`.sunil.spectre.ui.activity.artistdetail

import `in`.sunil.spectre.databinding.ActivityArtistDetailBinding
import android.app.Activity

/**
 * Created by Sunil on 10/6/18.
 */

class ArtistDetailActivityService : IArtistDetailActivityService {

    private val activity: Activity
    private val artistDetailBinding: ActivityArtistDetailBinding

    constructor(activity: Activity, artistDetailBinding: ActivityArtistDetailBinding) {

        this.activity = activity
        this.artistDetailBinding = artistDetailBinding
    }

    override fun closeArtistDetail() {

        activity.finish()
    }

}