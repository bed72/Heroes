package github.bed72.core.usecase

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import github.bed72.core.domain.model.Comic
import github.bed72.core.usecase.base.UseCase
import github.bed72.core.usecase.base.ResultStatus
import github.bed72.core.data.repository.characters.CharacterRepository
import github.bed72.core.domain.model.Event
import github.bed72.core.usecase.GetCharacterCategoriesUseCase.GetCharacterCategoriesParams
import github.bed72.core.usecase.base.CoroutinesDispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

interface GetCharacterCategoriesUseCase {
    operator fun invoke(params: GetCharacterCategoriesParams): Flow<ResultStatus<Pair<List<Comic>, List<Event>>>>

    data class GetCharacterCategoriesParams(val characterId: Int)
}

class GetCharacterCategoriesUseCaseImpl @Inject constructor(
    private val dispatchers: CoroutinesDispatchers,
    private val characterRepository: CharacterRepository
) : GetCharacterCategoriesUseCase, UseCase<GetCharacterCategoriesParams, Pair<List<Comic>, List<Event>>>() {
    override suspend fun doWork(params: GetCharacterCategoriesParams): ResultStatus<Pair<List<Comic>, List<Event>>> =
        withContext(dispatchers.io()) {
            val comicsDeferred = async { characterRepository.getComics(params.characterId) }
            val eventsDeferred = async { characterRepository.getEvents(params.characterId) }

            val comics = comicsDeferred.await()
            val events = eventsDeferred.await()

            ResultStatus.Success(comics to events)
        }
}