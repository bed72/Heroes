package github.bed72.bedapp.presentation.detail.viewholders

import github.bed72.bedapp.R
import android.view.ViewGroup
import android.widget.ImageView
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import github.bed72.bedapp.databinding.ItemChildDetailBinding
import github.bed72.bedapp.framework.imageloader.usecase.ImageLoader
import github.bed72.bedapp.presentation.detail.entities.DetailChildViewEntity

class DetailChildViewHolder(
    itemBinding: ItemChildDetailBinding,
    private val imageLoader: ImageLoader
) : RecyclerView.ViewHolder(itemBinding.root) {

    private val imageCategory: ImageView = itemBinding.imageItemCategory

    fun bind(detailChildViewEntity: DetailChildViewEntity) {
        imageLoader.load(imageCategory, detailChildViewEntity.imageUrl)
    }

    companion object {
        fun create(
            parent: ViewGroup,
            imageLoader: ImageLoader
        ): DetailChildViewHolder {
            val itemBinding = ItemChildDetailBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            return DetailChildViewHolder(itemBinding, imageLoader)
        }
    }
}