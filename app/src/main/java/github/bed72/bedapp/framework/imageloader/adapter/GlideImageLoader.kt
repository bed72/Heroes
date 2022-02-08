package github.bed72.bedapp.framework.imageloader.adapter

import android.widget.ImageView
import com.bumptech.glide.Glide
import github.bed72.bedapp.framework.imageloader.usecase.ImageLoader
import javax.inject.Inject

class GlideImageLoader @Inject constructor() : ImageLoader {
    override fun load(imageView: ImageView, imageUrl: String, fallback: Int) {
        Glide.with(imageView.rootView)
            .load(imageUrl)
            .error(fallback)
            .fallback(fallback)
            .into(imageView)
    }
}