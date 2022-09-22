package github.bed72.bedapp.presentation.detail.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import github.bed72.bedapp.databinding.ItemChildDetailBinding
import github.bed72.bedapp.framework.imageloader.usecase.ImageLoader
import github.bed72.bedapp.presentation.detail.data.DetailChildViewItem

class DetailChildViewHolder(
    itemBinding: ItemChildDetailBinding,
    private val imageLoader: ImageLoader
) : RecyclerView.ViewHolder(itemBinding.root) {

    private val imageCategory: ImageView = itemBinding.imageItemCategory

    fun bind(detailChildViewItem: DetailChildViewItem) {
        imageLoader.load(imageCategory, detailChildViewItem.imageUrl)
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
