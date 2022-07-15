package github.bed72.core.usecase

import javax.inject.Inject

import kotlinx.coroutines.flow.Flow

import androidx.paging.PagingData
import androidx.paging.PagingConfig

import github.bed72.core.domain.model.Character
import github.bed72.core.usecase.base.PagingUseCase
import github.bed72.core.data.repository.characters.CharacterRepository
import github.bed72.core.data.repository.storage.StorageRepository
import github.bed72.core.usecase.GetCharactersUseCase.GetCharactersParams
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

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
