package github.bed72.core.data.repository.characters

import github.bed72.core.domain.model.Comic
import github.bed72.core.domain.model.CharacterPaging
import github.bed72.core.domain.model.Event
// Para fonte de dados remota
interface CharacterRemoteDataSource {

    suspend fun fetchComics(characterId: Int): List<Comic>

    suspend fun fetchEvents(characterId: Int): List<Event>

    suspend fun fetchCharacters(queries: Map<String, String>): CharacterPaging
}