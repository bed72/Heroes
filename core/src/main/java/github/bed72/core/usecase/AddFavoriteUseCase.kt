package github.bed72.core.usecase

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import github.bed72.core.usecase.base.UseCase
import github.bed72.core.domain.model.Character
import github.bed72.core.usecase.base.ResultStatus
import github.bed72.core.usecase.base.CoroutinesDispatchers
import github.bed72.core.usecase.AddFavoriteUseCase.AddFavoriteParams
import github.bed72.core.data.repository.favorites.FavoriteRepository

interface AddFavoriteUseCase {
    operator fun invoke(params: AddFavoriteParams): Flow<ResultStatus<Unit>>

    data class AddFavoriteParams(val characterId: Int, val name: String, val imageUrl: String)
}

class AddFavoriteUseCaseImpl @Inject constructor(
    private val dispatchers: CoroutinesDispatchers,
    private val favoriteRepository: FavoriteRepository
) : UseCase<AddFavoriteParams, Unit>(), AddFavoriteUseCase {

    override suspend fun doWork(params: AddFavoriteParams): ResultStatus<Unit> =
        withContext(dispatchers.io()) {
            favoriteRepository.saveFavorite(
                Character(params.characterId, params.name, params.imageUrl)
            )

            ResultStatus.Success(Unit)
        }
}