package github.bed72.bedapp.framework

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import github.bed72.core.domain.model.Character
import github.bed72.core.data.repository.favorites.FavoriteRepository
import github.bed72.core.data.repository.favorites.FavoriteLocalDataSource

class FavoriteRepositoryImpl @Inject constructor(
    private val localDataSource: FavoriteLocalDataSource
) : FavoriteRepository {
    override fun getAllFavorite(): Flow<List<Character>> =
        localDataSource.getAllFavorite()

    override suspend fun saveFavorite(character: Character) {
        localDataSource.saveFavorite(character)
    }

    override suspend fun deleteFavorite(character: Character) {
        localDataSource.deleteFavorite(character)
    }
}