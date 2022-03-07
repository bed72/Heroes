package github.bed72.bedapp.presentation.detail.adapters

import android.view.ViewGroup
import github.bed72.bedapp.framework.imageloader.usecase.ImageLoader
import github.bed72.bedapp.presentation.detail.entities.DetailParentViewEntity
import github.bed72.bedapp.presentation.detail.viewholders.DetailParentViewHolder

class DetailParentAdapter(
    private val imageLoader: ImageLoader,
    private val detailParentList: List<DetailParentViewEntity>
) : DetailNestedBaseAdapter<DetailParentViewEntity, DetailParentViewHolder>(detailParentList) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DetailParentViewHolder.create(parent, imageLoader)

    override fun onBindViewHolder(holder: DetailParentViewHolder, position: Int) {
        holder.bind(detailParentList[position])
    }
}


