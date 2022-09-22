package github.bed72.core.usecase

import github.bed72.core.data.repository.favorites.FavoritesRepository
import github.bed72.core.domain.model.Character
import github.bed72.core.usecase.AddFavoriteUseCase.AddFavoriteParams
import github.bed72.core.usecase.base.CoroutinesDispatchers
import github.bed72.core.usecase.base.ResultStatus
import github.bed72.core.usecase.base.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface AddFavoriteUseCase {
    operator fun invoke(params: AddFavoriteParams): Flow<ResultStatus<Unit>>

    data class AddFavoriteParams(val characterId: Int, val name: String, val imageUrl: String)
}

class AddFavoriteUseCaseImpl @Inject constructor(
    private val dispatchers: CoroutinesDispatchers,
    private val favoritesRepository: FavoritesRepository
) : UseCase<AddFavoriteParams, Unit>(), AddFavoriteUseCase {
    override suspend fun doWork(params: AddFavoriteParams): ResultStatus<Unit> =
        withContext(dispatchers.io()) {
            favoritesRepository.saveFavorite(
                Character(params.characterId, params.name, params.imageUrl)
            )

            ResultStatus.Success(Unit)
        }
}
