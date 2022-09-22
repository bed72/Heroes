package github.bed72.core.usecase

import github.bed72.core.data.repository.favorites.FavoritesRepository
import github.bed72.core.usecase.CheckFavoriteUseCase.CheckFavoriteParams
import github.bed72.core.usecase.base.CoroutinesDispatchers
import github.bed72.core.usecase.base.ResultStatus
import github.bed72.core.usecase.base.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface CheckFavoriteUseCase {
    operator fun invoke(params: CheckFavoriteParams): Flow<ResultStatus<Boolean>>

    data class CheckFavoriteParams(val characterId: Int)
}

class CheckFavoriteUseCaseImpl @Inject constructor(
    private val dispatchers: CoroutinesDispatchers,
    private val favoritesRepository: FavoritesRepository
) : UseCase<CheckFavoriteParams, Boolean>(), CheckFavoriteUseCase {
    override suspend fun doWork(params: CheckFavoriteParams): ResultStatus<Boolean> =
        withContext(dispatchers.io()) {
            val isFavorite = favoritesRepository.isFavorite(params.characterId)

            ResultStatus.Success(isFavorite)
        }
}
