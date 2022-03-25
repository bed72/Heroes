package github.bed72.bedapp.framework

import javax.inject.Inject
import androidx.paging.PagingSource
import github.bed72.core.domain.model.Comic
import github.bed72.core.domain.model.Character
import github.bed72.core.data.repository.characters.CharacterRepository
import github.bed72.bedapp.framework.paging.CharactersPagingSource
import github.bed72.core.data.repository.characters.CharacterRemoteDataSource
import github.bed72.core.domain.model.Event

class CharacterRepositoryImpl @Inject constructor(
    private val remoteDataSource: CharacterRemoteDataSource
) : CharacterRepository {

    override suspend fun getComics(characterId: Int): List<Comic> =
        remoteDataSource.fetchComics(characterId)

    override suspend fun getEvents(characterId: Int): List<Event> =
        remoteDataSource.fetchEvents(characterId)

    override fun getCharacters(query: String): PagingSource<Int, Character> =
        CharactersPagingSource(query, remoteDataSource)
}