package `in`.sunil.spectre.network

/**
 * Created by Sunil on 9/28/18.
 */

class ProgressInfo {

    val url: String
    val progress: Int
    val complete: Boolean

    constructor(url: String, progress: Int, complete: Boolean) {
        this.url = url
        this.progress = progress
        this.complete = complete
    }
}