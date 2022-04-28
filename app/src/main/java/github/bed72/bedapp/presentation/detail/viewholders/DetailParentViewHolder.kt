package github.bed72.bedapp.presentation.detail.viewholders

import android.widget.TextView

import android.view.ViewGroup
import android.view.LayoutInflater

import androidx.recyclerview.widget.RecyclerView

import github.bed72.bedapp.databinding.ItemParentDetailBinding
import github.bed72.bedapp.framework.imageloader.usecase.ImageLoader
import github.bed72.bedapp.presentation.detail.adapters.DetailChildAdapter
import github.bed72.bedapp.presentation.detail.data.DetailParentViewItem

class DetailParentViewHolder(
    itemBinding: ItemParentDetailBinding,
    private val imageLoader: ImageLoader
) : RecyclerView.ViewHolder(itemBinding.root) {

    private val category: TextView = itemBinding.textItemCategory
    private val details: RecyclerView = itemBinding.recyclerChildDetail

    fun bind(detailParentViewItem: DetailParentViewItem) {
        category.text = itemView.context.getString(detailParentViewItem.categoryStringResId)
        details.run {
            setHasFixedSize(true)
            adapter = DetailChildAdapter(imageLoader, detailParentViewItem.detailChildViewItem)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            imageLoader: ImageLoader
        ): DetailParentViewHolder {
            val itemBinding = ItemParentDetailBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            return DetailParentViewHolder(itemBinding, imageLoader)
        }
    }
}


