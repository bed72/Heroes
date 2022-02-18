package github.bed72.core.data.repository

import androidx.paging.PagingSource
import github.bed72.core.domain.model.Character
import github.bed72.core.domain.model.Comic

interface CharactersRepository {
    suspend fun getComics(characterId: Int): List<Comic>
    fun getCharacters(query: String): PagingSource<Int, Character>
}