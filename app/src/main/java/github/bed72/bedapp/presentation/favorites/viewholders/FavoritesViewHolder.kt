package github.bed72.bedapp.presentation.favorites.viewholders

import android.widget.TextView
import android.widget.ImageView

import android.view.ViewGroup
import android.view.LayoutInflater

import github.bed72.bedapp.databinding.ItemCharacterBinding
import github.bed72.bedapp.presentation.favorites.data.FavoriteItem
import github.bed72.bedapp.framework.imageloader.usecase.ImageLoader
import github.bed72.bedapp.presentation.common.recycler.GenericViewHolder

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