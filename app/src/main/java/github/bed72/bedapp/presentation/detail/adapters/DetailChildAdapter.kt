package github.bed72.bedapp.presentation.detail.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import github.bed72.bedapp.framework.imageloader.usecase.ImageLoader
import github.bed72.bedapp.presentation.detail.data.DetailChildViewItem
import github.bed72.bedapp.presentation.detail.viewholders.DetailChildViewHolder

class DetailChildAdapter(
    private val imageLoader: ImageLoader,
    private val detailChildList: List<DetailChildViewItem>
) : RecyclerView.Adapter<DetailChildViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DetailChildViewHolder.create(parent, imageLoader)

    override fun onBindViewHolder(holder: DetailChildViewHolder, position: Int) {
        holder.bind(detailChildList[position])
    }

    override fun getItemCount() = detailChildList.size
}
