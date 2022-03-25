package github.bed72.core.data.repository.characters

import androidx.paging.PagingSource
import github.bed72.core.domain.model.Character
import github.bed72.core.domain.model.Comic
import github.bed72.core.domain.model.Event

interface CharacterRepository {

    suspend fun getComics(characterId: Int): List<Comic>

    suspend fun getEvents(characterId: Int): List<Event>

    fun getCharacters(query: String): PagingSource<Int, Character>
}