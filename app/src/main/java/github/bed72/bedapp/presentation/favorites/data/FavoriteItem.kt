package github.bed72.bedapp.presentation.favorites.data

import github.bed72.bedapp.presentation.common.recycler.ListItem

data class FavoriteItem(
    val name: String,
    val imageUrl: String,
    val characterId: Int,

    override val key: Long = characterId.toLong()
) : ListItem
