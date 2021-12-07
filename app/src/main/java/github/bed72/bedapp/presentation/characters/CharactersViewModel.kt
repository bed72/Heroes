package github.bed72.bedapp.presentation.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import github.bed72.core.domain.model.Character
import github.bed72.core.usecase.GetCharactersUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase
) : ViewModel() {

    fun charactersPagingData(query: String): Flow<PagingData<Character>> =
        getCharactersUseCase(
            GetCharactersUseCase.GetCharactersParams(
                query,
                getPageConfig()
            )
        ).cachedIn(viewModelScope)

    private fun getPageConfig() = PagingConfig(
        pageSize = 20
    )
}