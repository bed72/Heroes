package github.bed72.core.data.repository.favorites

import kotlinx.coroutines.flow.Flow
import github.bed72.core.domain.model.Character
// Para fonte de dados local 
interface FavoriteLocalDataSource {
    fun getAllFavorite(): Flow<List<Character>>

    suspend fun isFavorite(characterId: Int): Boolean

    suspend fun saveFavorite(character: Character)

    suspend fun deleteFavorite(character: Character)
}