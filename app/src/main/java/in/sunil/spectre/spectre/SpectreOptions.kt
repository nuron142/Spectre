package `in`.sunil.spectre.spectre

import android.support.annotation.DrawableRes

/**
 * Created by Sunil on 9/29/18.
 */

class SpectreOptions {

    var imageUrl: String

    @DrawableRes
    var placeHolder: Int? = null

    var loadingFromNetwork: ((Boolean) -> Unit)? = null
    var progressListener: ((Int) -> Unit)? = null

    constructor(imageUrl: String) {
        this.imageUrl = imageUrl
    }

    fun placeHolder(placeHolder: Int): SpectreOptions {
        this.placeHolder = placeHolder
        return this
    }

    fun progressListener(progressListener: (Int) -> Unit): SpectreOptions {
        this.progressListener = progressListener
        return this
    }

    fun loadingFromNetwork(loadingFromNetwork: (Boolean) -> Unit): SpectreOptions {
        this.loadingFromNetwork = loadingFromNetwork
        return this
    }
}