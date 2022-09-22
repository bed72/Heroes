package github.bed72.bedapp.framework.local.room

import github.bed72.bedapp.framework.db.daos.FavoriteDao
import github.bed72.bedapp.framework.db.entities.FavoriteEntity
import github.bed72.bedapp.framework.db.entities.toCharactersModel
import github.bed72.core.data.repository.favorites.FavoriteLocalDataSource
import github.bed72.core.domain.model.Character
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomFavoritesDataSource @Inject constructor(
    private val favoriteDao: FavoriteDao
) : FavoriteLocalDataSource {
    override fun getAllFavorite(): Flow<List<Character>> =
        favoriteDao.loadFavorites().map { favorites -> favorites.toCharactersModel() }

    override suspend fun isFavorite(characterId: Int): Boolean =
        favoriteDao.hasFavorite(characterId) != null

    override suspend fun saveFavorite(character: Character) {
        favoriteDao.insertFavorite(character.toFavoriteEntity())
    }

    override suspend fun deleteFavorite(character: Character) {
        favoriteDao.deleteFavorite(character.toFavoriteEntity())
    }

    private fun Character.toFavoriteEntity() = FavoriteEntity(id, name, imageUrl)
}
