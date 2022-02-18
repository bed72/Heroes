package github.bed72.bedapp.framework.remote

import github.bed72.bedapp.framework.network.MarvelApi
import github.bed72.bedapp.framework.network.response.DataWrapperResponse
import github.bed72.bedapp.framework.network.response.toCharacterModel
import github.bed72.bedapp.framework.network.response.toComicModel
import github.bed72.core.data.repository.CharactersRemoteDataSource
import github.bed72.core.domain.model.CharacterPaging
import github.bed72.core.domain.model.Comic
import javax.inject.Inject

class RetrofitCharactersDataSource @Inject constructor(
    private val marvelApi: MarvelApi
) : CharactersRemoteDataSource {
    override suspend fun fetchComics(characterId: Int): List<Comic> =
        marvelApi.getComics(characterId).data.results.map { comics ->
            comics.toComicModel()
        }

    override suspend fun fetchCharacters(queries: Map<String, String>): CharacterPaging {
        val (offset, total, results) = marvelApi.getCharacters(queries).data

        return CharacterPaging(
            offset,
            total,
            results.map { it.toCharacterModel() }
        )
    }
}
