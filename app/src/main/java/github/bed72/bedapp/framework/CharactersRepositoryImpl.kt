package github.bed72.bedapp.framework

import androidx.paging.PagingSource
import github.bed72.bedapp.framework.network.response.DataWrapperResponse
import github.bed72.core.data.repository.CharactersRemoteDataSource
import github.bed72.core.data.repository.CharactersRepository
import github.bed72.core.domain.entity.Character
import javax.inject.Inject

class CharactersRepositoryImpl @Inject constructor(
    private val remoteDataSource: CharactersRemoteDataSource<DataWrapperResponse>
) : CharactersRepository {

    override fun getCharacters(query: String): PagingSource<Int, Character> {
        return
    }

}