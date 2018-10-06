package `in`.sunil.spectre.ui.activity.search.viewmodels

import `in`.sunil.spectre.ui.activity.search.SearchActivityViewModel
import `in`.sunil.spectre.ui.adapter.ViewModel
import android.databinding.ObservableField

/**
 * Created by Sunil on 10/5/18.
 */
class SearchHeaderViewModel : ViewModel {

    val title = ObservableField<String>("")

    constructor(title: String) {

        this.title.set(title)
    }

    override fun getType(): Int {

        return SearchActivityViewModel.VIEW_TYPE_HEADER
    }
}