package github.bed72.bedapp.framework.imageloader.usecase

import android.widget.ImageView
import androidx.annotation.DrawableRes
import github.bed72.bedapp.R

interface ImageLoader {
    fun load(
        imageView: ImageView,
        imageUrl: String,
        @DrawableRes fallback: Int = R.drawable.ic_img_loading_error,
        @DrawableRes placeholder: Int = R.drawable.ic_img_placeholder
    )
}