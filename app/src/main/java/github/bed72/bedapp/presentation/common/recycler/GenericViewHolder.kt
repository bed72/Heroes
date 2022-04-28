package github.bed72.bedapp.presentation.common.recycler

import androidx.viewbinding.ViewBinding

import androidx.recyclerview.widget.RecyclerView

abstract class GenericViewHolder<T>(
    item: ViewBinding
) : RecyclerView.ViewHolder(item.root) {
    abstract fun bind(data: T)
}