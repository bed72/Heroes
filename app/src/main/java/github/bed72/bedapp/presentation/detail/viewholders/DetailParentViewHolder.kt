package github.bed72.bedapp.presentation.detail.viewholders

import android.view.ViewGroup
import android.widget.TextView
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import github.bed72.bedapp.databinding.ItemParentDetailBinding
import github.bed72.bedapp.framework.imageloader.usecase.ImageLoader
import github.bed72.bedapp.presentation.detail.adapters.DetailChildAdapter
import github.bed72.bedapp.presentation.detail.entities.DetailParentViewEntity

class DetailParentViewHolder(
    itemBinding: ItemParentDetailBinding,
    private val imageLoader: ImageLoader
) : RecyclerView.ViewHolder(itemBinding.root) {

    private val category: TextView = itemBinding.textItemCategory
    private val details: RecyclerView = itemBinding.recyclerChildDetail

    fun bind(detailParentViewEntity: DetailParentViewEntity) {
        category.text = itemView.context.getString(detailParentViewEntity.categoryStringResId)
        details.run {
            setHasFixedSize(true)
            adapter = DetailChildAdapter(imageLoader, detailParentViewEntity.detailChildViewEntity)
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