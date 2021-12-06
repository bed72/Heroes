package github.bed72.bedapp.framework.remote

import github.bed72.bedapp.framework.network.MarvelApi
import github.bed72.bedapp.framework.network.response.DataWrapperResponse
import github.bed72.core.data.repository.CharactersRemoteDataSource
import javax.inject.Inject

class RetrofitCharactersDataSource @Inject constructor(
    private val marvelApi: MarvelApi
) : CharactersRemoteDataSource<DataWrapperResponse> {

    override suspend fun fetchCharacters(queries: Map<String, String>) =
        marvelApi.getCharacters(queries)

}