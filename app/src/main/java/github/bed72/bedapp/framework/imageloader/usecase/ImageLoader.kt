package github.bed72.bedapp.framework.imageloader.usecase

import android.widget.ImageView
import androidx.annotation.DrawableRes

interface ImageLoader {
    fun load(imageView: ImageView, imageUrl: String, @DrawableRes fallback: Int)
}