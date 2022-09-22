package github.bed72.bedapp.framework.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import github.bed72.core.data.repository.characters.CharacterRemoteDataSource
import github.bed72.core.domain.model.Character

class CharactersPagingSource(
    private val query: String,
    private val remoteDataSource: CharacterRemoteDataSource
) : PagingSource<Int, Character>() {

    @Suppress("TooGenericExceptionCaught")
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        return try {
            val offsetParams = params.key ?: 0
            val queries = hashMapOf(
                "offset" to offsetParams.toString()
            )

            if (queries.isNotEmpty() && query.isNotEmpty())
                queries["nameStartsWith"] = query

            val (offset, total, characters) = remoteDataSource.fetchCharacters(queries)

            LoadResult.Page(
                prevKey = null,
                data = characters,
                nextKey = if (offset < total) offset + LIMIT else null
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
