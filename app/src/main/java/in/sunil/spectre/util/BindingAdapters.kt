package `in`.sunil.spectre.util

import android.databinding.BindingAdapter
import android.view.View

/**
 * Created by Sunil on 10/6/18.
 */

@BindingAdapter("android:onClick")
fun setOnClick(view: View, onClickAction: (() -> Unit)?) {

    view.setOnClickListener {
        onClickAction?.invoke()
    }
}

@BindingAdapter("android:visibility")
fun bindVisibility(view: View, visible: Boolean?) {

    view.visibility = if (visible != null && visible) View.VISIBLE else View.GONE
}
