package github.bed72.core.usecase

import javax.inject.Inject

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

import github.bed72.core.usecase.base.UseCase
import github.bed72.core.usecase.base.ResultStatus
import github.bed72.core.usecase.base.CoroutinesDispatchers
import github.bed72.core.data.repository.favorites.FavoriteRepository
import github.bed72.core.usecase.CheckFavoriteUseCase.CheckFavoriteParams

interface CheckFavoriteUseCase {
    operator fun invoke(params: CheckFavoriteParams): Flow<ResultStatus<Boolean>>

    data class CheckFavoriteParams(val characterId: Int)
}

class CheckFavoriteUseCaseImpl @Inject constructor(
    private val dispatchers: CoroutinesDispatchers,
    private val favoriteRepository: FavoriteRepository
) : UseCase<CheckFavoriteParams, Boolean>(), CheckFavoriteUseCase {
    override suspend fun doWork(params: CheckFavoriteParams): ResultStatus<Boolean> =
        withContext(dispatchers.io()) {
            val isFavorite = favoriteRepository.isFavorite(params.characterId)

            ResultStatus.Success(isFavorite)
        }
}