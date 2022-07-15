package github.bed72.core.data.repository.characters

import kotlinx.coroutines.flow.Flow

import androidx.paging.PagingData
import androidx.paging.PagingConfig

import github.bed72.core.domain.model.Comic
import github.bed72.core.domain.model.Event
import github.bed72.core.domain.model.Character

interface CharacterRepository {

    suspend fun getComics(characterId: Int): List<Comic>

    suspend fun getEvents(characterId: Int): List<Event>

    fun getCharacters(
        query: String,
        orderBy: String,
        pagingConfig: PagingConfig
    ): Flow<PagingData<Character>>
}