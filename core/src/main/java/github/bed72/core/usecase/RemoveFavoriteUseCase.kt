package github.bed72.core.usecase

import github.bed72.core.data.repository.favorites.FavoritesRepository
import github.bed72.core.domain.model.Character
import github.bed72.core.usecase.RemoveFavoriteUseCase.RemoveFavoriteParams
import github.bed72.core.usecase.base.CoroutinesDispatchers
import github.bed72.core.usecase.base.ResultStatus
import github.bed72.core.usecase.base.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface RemoveFavoriteUseCase {
    operator fun invoke(params: RemoveFavoriteParams): Flow<ResultStatus<Unit>>

    data class RemoveFavoriteParams(val characterId: Int, val name: String, val imageUrl: String)
}

class RemoveFavoriteUseCaseImpl @Inject constructor(
    private val dispatchers: CoroutinesDispatchers,
    private val favoritesRepository: FavoritesRepository
) : UseCase<RemoveFavoriteParams, Unit>(), RemoveFavoriteUseCase {
    override suspend fun doWork(params: RemoveFavoriteParams): ResultStatus<Unit> =
        withContext(dispatchers.io()) {
            favoritesRepository.deleteFavorite(
                Character(params.characterId, params.name, params.imageUrl)
            )

            ResultStatus.Success(Unit)
        }
}
