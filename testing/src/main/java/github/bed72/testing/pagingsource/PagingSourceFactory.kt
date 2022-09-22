package github.bed72.testing.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import github.bed72.core.domain.model.Character

class PagingSourceFactory {

    fun create(heroes: List<Character>) = object : PagingSource<Int, Character>() {
        override fun getRefreshKey(state: PagingState<Int, Character>) = 1

        override suspend fun load(params: LoadParams<Int>) = LoadResult.Page(
            data = heroes,
            prevKey = null,
            nextKey = 20
        )
    }
}
