package github.bed72.bedapp.presentation.detail.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import github.bed72.bedapp.framework.imageloader.usecase.ImageLoader
import github.bed72.bedapp.presentation.detail.data.DetailParentViewItem
import github.bed72.bedapp.presentation.detail.viewholders.DetailParentViewHolder

class DetailParentAdapter(
    private val imageLoader: ImageLoader,
    private val detailParentList: List<DetailParentViewItem>
) : RecyclerView.Adapter<DetailParentViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DetailParentViewHolder.create(parent, imageLoader)

    override fun onBindViewHolder(holder: DetailParentViewHolder, position: Int) {
        holder.bind(detailParentList[position])
    }

    override fun getItemCount() = detailParentList.size
}
