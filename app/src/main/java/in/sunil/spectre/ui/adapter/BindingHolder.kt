package `in`.sunil.spectre.ui.adapter

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView

/**
 * Created by Sunil on 10/5/18.
 */
class BindingHolder : RecyclerView.ViewHolder {

    var binding: ViewDataBinding

    constructor(binding: ViewDataBinding) : super(binding.root) {

        this.binding = binding
    }
}