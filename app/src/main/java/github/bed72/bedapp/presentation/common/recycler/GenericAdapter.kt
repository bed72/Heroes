package github.bed72.bedapp.presentation.common.recycler

import android.view.ViewGroup

import androidx.recyclerview.widget.ListAdapter

// Copia e cola onde e chamado
inline fun <T : ListItem, VH: GenericViewHolder<T>> getGenericAdapterOf(
    crossinline createViewHolder: (ViewGroup) -> VH
): ListAdapter<T, VH> {
    val diffCallback = GenericDiffCallback<T>()

    return object : ListAdapter<T, VH>(diffCallback) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
            createViewHolder(parent)


        override fun onBindViewHolder(holder: VH, position: Int) {
            holder.bind(getItem(position))
        }
    }
}