package github.bed72.bedapp.framework.remote

import github.bed72.bedapp.framework.network.MarvelApi
import github.bed72.bedapp.framework.network.response.toCharacterModel
import github.bed72.bedapp.framework.network.response.toComicModel
import github.bed72.bedapp.framework.network.response.toEventModel
import github.bed72.core.data.repository.characters.CharacterRemoteDataSource
import github.bed72.core.domain.model.CharacterPaging
import github.bed72.core.domain.model.Comic
import github.bed72.core.domain.model.Event
import javax.inject.Inject

class RetrofitCharacterDataSource @Inject constructor(
    private val marvelApi: MarvelApi
) : CharacterRemoteDataSource {
    override suspend fun fetchComics(characterId: Int): List<Comic> =
        marvelApi.getComics(characterId).data.results.map { comics ->
            comics.toComicModel()
        }

    override suspend fun fetchEvents(characterId: Int): List<Event> =
        marvelApi.getEvents(characterId).data.results.map { events ->
            events.toEventModel()
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
