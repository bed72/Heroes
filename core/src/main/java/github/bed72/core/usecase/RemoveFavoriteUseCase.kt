package github.bed72.core.usecase

import javax.inject.Inject

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

import github.bed72.core.usecase.base.UseCase
import github.bed72.core.domain.model.Character
import github.bed72.core.usecase.base.ResultStatus
import github.bed72.core.usecase.base.CoroutinesDispatchers
import github.bed72.core.data.repository.favorites.FavoriteRepository
import github.bed72.core.usecase.RemoveFavoriteUseCase.RemoveFavoriteParams

interface RemoveFavoriteUseCase {
    operator fun invoke(params: RemoveFavoriteParams): Flow<ResultStatus<Unit>>

    data class RemoveFavoriteParams(val characterId: Int, val name: String, val imageUrl: String)
}

class RemoveFavoriteUseCaseImpl @Inject constructor(
    private val dispatchers: CoroutinesDispatchers,
    private val favoriteRepository: FavoriteRepository
) : UseCase<RemoveFavoriteParams, Unit>(), RemoveFavoriteUseCase {
    override suspend fun doWork(params: RemoveFavoriteParams): ResultStatus<Unit> =
        withContext(dispatchers.io()) {
            favoriteRepository.deleteFavorite(
                Character(params.characterId, params.name, params.imageUrl)
            )

            ResultStatus.Success(Unit)
        }
}