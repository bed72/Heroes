package github.bed72.bedapp.presentation.favorites.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import github.bed72.bedapp.databinding.ItemCharacterBinding
import github.bed72.bedapp.framework.imageloader.usecase.ImageLoader
import github.bed72.bedapp.presentation.common.recycler.GenericViewHolder
import github.bed72.bedapp.presentation.favorites.data.FavoriteItem

class FavoritesViewHolder(
    item: ItemCharacterBinding,
    private val imageLoader: ImageLoader
) : GenericViewHolder<FavoriteItem>(item) {

    private val textName: TextView = item.textNameCharacter
    private val imageCharacter: ImageView = item.imageCharacter

    override fun bind(data: FavoriteItem) {
        textName.text = data.name
        imageLoader.load(imageCharacter, data.imageUrl)
    }

    companion object {
        fun create(parent: ViewGroup, imageLoader: ImageLoader): FavoritesViewHolder {
            val itemBinding = ItemCharacterBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)

            return FavoritesViewHolder(itemBinding, imageLoader)
        }
    }
}
