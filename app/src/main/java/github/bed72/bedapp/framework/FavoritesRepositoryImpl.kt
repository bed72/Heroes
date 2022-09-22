package github.bed72.bedapp.framework

import github.bed72.core.data.repository.favorites.FavoriteLocalDataSource
import github.bed72.core.data.repository.favorites.FavoritesRepository
import github.bed72.core.domain.model.Character
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoritesRepositoryImpl @Inject constructor(
    private val localDataSource: FavoriteLocalDataSource
) : FavoritesRepository {
    override fun getAllFavorite(): Flow<List<Character>> =
        localDataSource.getAllFavorite()

    override suspend fun isFavorite(characterId: Int): Boolean =
        localDataSource.isFavorite(characterId)

    override suspend fun saveFavorite(character: Character) {
        localDataSource.saveFavorite(character)
    }

    override suspend fun deleteFavorite(character: Character) {
        localDataSource.deleteFavorite(character)
    }
}
