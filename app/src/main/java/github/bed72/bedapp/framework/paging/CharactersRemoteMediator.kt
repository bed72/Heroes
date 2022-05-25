package github.bed72.bedapp.framework.paging

import java.io.IOException

import javax.inject.Inject

import retrofit2.HttpException

import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.room.withTransaction
import androidx.paging.RemoteMediator
import androidx.paging.ExperimentalPagingApi

import github.bed72.core.domain.model.Character
import github.bed72.bedapp.framework.db.AppDatabase
import github.bed72.bedapp.framework.db.entities.CharacterEntity
import github.bed72.bedapp.framework.db.entities.RemoteKeyEntity
import github.bed72.core.data.repository.characters.CharacterRemoteDataSource

private data class ExecuteTransactionParams(
    val offset: Int,
    val loadType: LoadType,
    val characters: List<Character>,
    val state: PagingState<Int, CharacterEntity>
)

@OptIn(ExperimentalPagingApi::class)
class CharactersRemoteMediator @Inject constructor(
    private val query: String,
    private val database: AppDatabase,
    private val remoteDataSource: CharacterRemoteDataSource
) : RemoteMediator<Int, CharacterEntity>() {

    private val characterDao = database.characterDao()
    private val remoteKeyDao = database.remoteKeyDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterEntity>
    ): MediatorResult {
        return try {
            val buildOffset = buildOffset(loadType)
            if (buildOffset == PREPEND_OR_APPEND) return MediatorResult.Success(true)

            val queries = buildQueries(buildOffset)
            val (offset, total, characters) = remoteDataSource.fetchCharacters(queries)

            executeTransaction(ExecuteTransactionParams(offset, loadType, characters, state))

            MediatorResult.Success(offset >= total)
        } catch (error: IOException) {
            MediatorResult.Error(error)
        } catch (error: HttpException) {
            MediatorResult.Error(error)
        }
    }

    private suspend fun buildOffset(loadType: LoadType): Int = when (loadType) {
        LoadType.REFRESH -> REFRESH
        LoadType.PREPEND -> PREPEND_OR_APPEND
        LoadType.APPEND ->  getRemoteKey().nextOffset ?: PREPEND_OR_APPEND
    }

    private suspend fun getRemoteKey(): RemoteKeyEntity = database.withTransaction {
        remoteKeyDao.remoteKey()
    }

    private suspend fun executeTransaction(params: ExecuteTransactionParams) {
        database.withTransaction {
            if (params.loadType == LoadType.REFRESH) clearAll()

            remoteKeyDaoInsertOrReplace(params.offset, params.state)

            characterDao.insertAll(buildCharactersEntity(params.characters))
        }
    }

    private suspend fun clearAll() {
        remoteKeyDao.clearAll()
        characterDao.clearAll()
    }

    private suspend fun remoteKeyDaoInsertOrReplace(
        offset: Int,
        state: PagingState<Int, CharacterEntity>
    ) {
        remoteKeyDao.insertOrReplace(
            RemoteKeyEntity(nextOffset = offset + state.config.pageSize)
        )
    }

    private fun buildCharactersEntity(characters: List<Character>) =
        characters.map { (id, name, imageUrl) ->
            CharacterEntity(
                id = id,
                name = name,
                imageUrl = imageUrl
            )
    }

    private fun buildQueries(offset: Int): HashMap<String, String> {
        val queries = hashMapOf(
            "offset" to offset.toString()
        )

        if (queries.isNotEmpty() && query.isNotEmpty())
            queries["nameStartsWith"] = query

        return queries
    }
    
    companion object {
        private const val REFRESH = 0
        private const val PREPEND_OR_APPEND = 1
    }
}

