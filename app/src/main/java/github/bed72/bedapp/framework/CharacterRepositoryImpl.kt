package github.bed72.bedapp.framework

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import github.bed72.bedapp.framework.db.AppDatabase
import github.bed72.bedapp.framework.db.entities.CharacterEntity
import github.bed72.bedapp.framework.paging.CharactersRemoteMediator
import github.bed72.core.data.repository.characters.CharacterRemoteDataSource
import github.bed72.core.data.repository.characters.CharacterRepository
import github.bed72.core.domain.model.Character
import github.bed72.core.domain.model.Comic
import github.bed72.core.domain.model.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

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
