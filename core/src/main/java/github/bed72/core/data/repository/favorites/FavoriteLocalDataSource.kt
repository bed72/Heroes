package github.bed72.core.data.repository.favorites

import github.bed72.core.domain.model.Character
import kotlinx.coroutines.flow.Flow
// Para fonte de dados local
interface FavoriteLocalDataSource {
    fun getAllFavorite(): Flow<List<Character>>

    suspend fun isFavorite(characterId: Int): Boolean

    suspend fun saveFavorite(character: Character)

    suspend fun deleteFavorite(character: Character)
}
