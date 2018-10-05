package `in`.sunil.spectre.ui.adapter

import `in`.sunil.spectre.ui.adapter.SearchAdapter.Companion.VIEW_TYPE_TRACK
import android.databinding.ObservableField

/**
 * Created by Sunil on 10/5/18.
 */
class SearchTrackViewModel : ViewModel {

    val name = ObservableField<String>("")

    constructor(name: String) {
        this.name.set(name)
    }

    override fun getType(): Int {
        return VIEW_TYPE_TRACK
    }

}