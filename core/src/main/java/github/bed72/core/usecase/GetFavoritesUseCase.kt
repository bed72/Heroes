package github.bed72.core.usecase

import github.bed72.core.data.repository.favorites.FavoritesRepository
import github.bed72.core.domain.model.Character
import github.bed72.core.usecase.base.CoroutinesDispatchers
import github.bed72.core.usecase.base.FlowUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface GetFavoritesUseCase {
    suspend operator fun invoke(params: Unit = Unit): Flow<List<Character>>
}

class GetFavoritesUseCaseImpl @Inject constructor(
    private val dispatchers: CoroutinesDispatchers,
    private val favoritesRepository: FavoritesRepository
) : FlowUseCase<Unit, List<Character>>(), GetFavoritesUseCase {
    override suspend fun createFlowObservable(params: Unit): Flow<List<Character>> =
        withContext(dispatchers.io()) {
            favoritesRepository.getAllFavorite()
        }
}
