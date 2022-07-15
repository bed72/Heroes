package github.bed72.bedapp.framework

import javax.inject.Inject

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow

import androidx.paging.map
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingConfig
import androidx.paging.ExperimentalPagingApi

import github.bed72.core.domain.model.Comic
import github.bed72.core.domain.model.Event
import github.bed72.core.domain.model.Character
import github.bed72.bedapp.framework.db.AppDatabase
import github.bed72.bedapp.framework.db.entities.CharacterEntity
import github.bed72.bedapp.framework.paging.CharactersRemoteMediator
import github.bed72.core.data.repository.characters.CharacterRepository
import github.bed72.core.data.repository.characters.CharacterRemoteDataSource

@OptIn(ExperimentalPagingApi::class)
class CharacterRepositoryImpl @Inject constructor(
    private val database: AppDatabase,
    private val remoteDataSource: CharacterRemoteDataSource
) : CharacterRepository {

    override suspend fun getComics(characterId: Int): List<Comic> =
        remoteDataSource.fetchComics(characterId)

    override suspend fun getEvents(characterId: Int): List<Event> =
        remoteDataSource.fetchEvents(characterId)

    override fun getCharacters(
        query: String,
        orderBy: String,
        pagingConfig: PagingConfig
    ): Flow<PagingData<Character>> = Pager(
        config = pagingConfig,
        remoteMediator = CharactersRemoteMediator(query, orderBy, database, remoteDataSource)
    ) {
        database.characterDao().pagingSource()
    }.flow.map { pagingData -> buildPagingData(pagingData) }

    private fun buildPagingData(pagingData: PagingData<CharacterEntity>) =
        pagingData.map { character -> buildCharacter(character) }

    private fun buildCharacter(character: CharacterEntity) =
        Character(
            character.id,
            character.name,
            character.imageUrl
        )
}