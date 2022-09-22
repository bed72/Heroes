package github.bed72.core.data.repository.characters

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import github.bed72.core.domain.model.Character
import github.bed72.core.domain.model.Comic
import github.bed72.core.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {

    suspend fun getComics(characterId: Int): List<Comic>

    suspend fun getEvents(characterId: Int): List<Event>

    fun getCharacters(
        query: String,
        orderBy: String,
        pagingConfig: PagingConfig
    ): Flow<PagingData<Character>>
}
