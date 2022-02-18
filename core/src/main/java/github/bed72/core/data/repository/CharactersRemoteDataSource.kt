package github.bed72.core.data.repository

import github.bed72.core.domain.model.Comic
import github.bed72.core.domain.model.CharacterPaging

interface CharactersRemoteDataSource {
    suspend fun fetchComics(characterId: Int): List<Comic>

    suspend fun fetchCharacters(queries: Map<String, String>): CharacterPaging
}