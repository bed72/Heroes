package github.bed72.core.usecase

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import github.bed72.core.domain.model.Comic
import github.bed72.core.usecase.base.UseCase
import github.bed72.core.usecase.base.ResultStatus
import github.bed72.core.data.repository.CharactersRepository
import github.bed72.core.usecase.GetComicsUseCase.GetComicsParams

interface GetComicsUseCase {
    operator fun invoke(params: GetComicsParams): Flow<ResultStatus<List<Comic>>>

    data class GetComicsParams(val characterId: Int)
}

class GetComicsUseCaseImpl @Inject constructor(
    private val charactersRepository: CharactersRepository
) : GetComicsUseCase, UseCase<GetComicsParams, List<Comic>>() {
    override suspend fun doWork(params: GetComicsParams): ResultStatus<List<Comic>> {
        val comics = charactersRepository.getComics(params.characterId)

        return ResultStatus.Success(comics)
    }
}