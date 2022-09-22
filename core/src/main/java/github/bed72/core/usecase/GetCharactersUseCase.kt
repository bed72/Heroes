package github.bed72.core.usecase

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import github.bed72.core.data.repository.characters.CharacterRepository
import github.bed72.core.data.repository.storage.StorageRepository
import github.bed72.core.domain.model.Character
import github.bed72.core.usecase.GetCharactersUseCase.GetCharactersParams
import github.bed72.core.usecase.base.PagingUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

interface GetCharactersUseCase {
    operator fun invoke(params: GetCharactersParams): Flow<PagingData<Character>>

    data class GetCharactersParams(val query: String, val pagingConfig: PagingConfig)
}

class GetCharactersUseCaseImpl @Inject constructor(
    private val storageRepository: StorageRepository,
    private val characterRepository: CharacterRepository
) : PagingUseCase<GetCharactersParams, Character>(), GetCharactersUseCase {

    override fun createFlowObservable(params: GetCharactersParams): Flow<PagingData<Character>> {
        // Fanzedo flow executar de forma sincrona
        val orderBy = runBlocking { storageRepository.sorting.first() }

        return characterRepository.getCharacters(params.query, orderBy, params.pagingConfig)
    }
}
