package github.bed72.core.usecase

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import github.bed72.core.domain.model.Comic
import github.bed72.core.usecase.base.UseCase
import github.bed72.core.usecase.base.ResultStatus
import github.bed72.core.data.repository.CharactersRepository
import github.bed72.core.domain.model.Event
import github.bed72.core.usecase.GetCharacterCategoriesUse.GetComicsParams
import github.bed72.core.usecase.base.AppCoroutinesDispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

interface GetCharacterCategoriesUse {
    operator fun invoke(params: GetComicsParams): Flow<ResultStatus<Pair<List<Comic>, List<Event>>>>

    data class GetComicsParams(val characterId: Int)
}

class GetCharacterCategoriesUseImpl @Inject constructor(
    private val dispatchers: AppCoroutinesDispatchers,
    private val charactersRepository: CharactersRepository
) : GetCharacterCategoriesUse, UseCase<GetComicsParams, Pair<List<Comic>, List<Event>>>() {
    override suspend fun doWork(params: GetComicsParams): ResultStatus<Pair<List<Comic>, List<Event>>> =
        withContext(dispatchers.io) {
            val comicsDeferred = async { charactersRepository.getComics(params.characterId) }
            val eventsDeferred = async { charactersRepository.getEvents(params.characterId) }

            val comics = comicsDeferred.await()
            val events = eventsDeferred.await()

            ResultStatus.Success(comics to events)
        }
}