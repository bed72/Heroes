package github.bed72.bedapp.framework.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import github.bed72.bedapp.framework.network.response.DataWrapperResponse
import github.bed72.bedapp.framework.network.response.toCharacterModel
import github.bed72.core.data.repository.CharactersRemoteDataSource
import github.bed72.core.domain.model.Character

class CharactersPagingSource(
    private val query: String,
    private val remoteDataSource: CharactersRemoteDataSource<DataWrapperResponse>
) : PagingSource<Int, Character>() {

    @Suppress("TooGenericExceptionCaught")
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        return try {
            val offset = params.key ?: 0
            val queries = hashMapOf(
                "offset" to offset.toString()
            )

            if (queries.isNotEmpty() && query.isNotEmpty())
                queries["nameStartsWith"] = query


            val response = remoteDataSource.fetchCharacters(queries)
            val responseOffset = response.data.offset
            val responseTotalCharacters = response.data.total

            LoadResult.Page(
                data = response.data.results.map { it.toCharacterModel() },
                prevKey = null,
                nextKey = if (responseOffset < responseTotalCharacters) responseOffset + LIMIT else null
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Character>) =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(LIMIT) ?: anchorPage?.nextKey?.minus(LIMIT)
        }

    companion object {
        private const val LIMIT = 20
    }
}