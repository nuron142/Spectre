package `in`.sunil.spectre.util

import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions

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

@BindingAdapter("image_url", "placeholder")
fun loadImage(imageView: ImageView, imageUrl: String?, placeholder: Drawable) {

    val glide = Glide.with(imageView.context)

    glide.load(imageUrl)
            .apply(RequestOptions
                    .centerCropTransform()
                    .placeholder(placeholder))
            .transition(withCrossFade())
            .into(imageView)
}
