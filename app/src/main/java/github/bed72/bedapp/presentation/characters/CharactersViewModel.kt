package github.bed72.bedapp.presentation.characters

import javax.inject.Inject

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow

import androidx.paging.cachedIn
import androidx.paging.PagingData
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.paging.PagingConfig
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.distinctUntilChanged

import dagger.hilt.android.lifecycle.HiltViewModel

import github.bed72.core.domain.model.Character
import github.bed72.core.usecase.GetCharactersUseCase
import github.bed72.core.usecase.base.CoroutinesDispatchers
import github.bed72.core.usecase.GetCharactersUseCase.GetCharactersParams
import github.bed72.bedapp.presentation.characters.CharactersViewModel.Actions.Sort
import github.bed72.bedapp.presentation.characters.CharactersViewModel.Actions.Search
import github.bed72.bedapp.presentation.characters.CharactersViewModel.States.SearchResult

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val coroutineDispatcher: CoroutinesDispatchers,
    private val getCharactersUseCase: GetCharactersUseCase
) : ViewModel() {

    private val action = MutableLiveData<Actions>()
    val state: LiveData<States> = action
        /**
         * Só irá notificar o estado do LiveData se os dados que estão nele forem diferentes
         * distinctUntilChanged()
         */
        // .
        .switchMap { actions ->
            when (actions) {
                is Search, Sort -> charactersPagingData().map {
                    SearchResult(it)
                }.asLiveData(coroutineDispatcher.main())
            }
        }

    private fun charactersPagingData(query: String = ""): Flow<PagingData<Character>> =
        getCharactersUseCase(
            GetCharactersParams(
                query,
                getPageConfig()
            )
        ).cachedIn(viewModelScope)

    private fun getPageConfig() = PagingConfig(
        pageSize = 20
    )

    fun search(query: String = "") {
        action.value = Search(query)
    }

    fun sort() {
        action.value = Sort
    }

    sealed class Actions {
        object Sort : Actions()
        data class Search(val query: String) : Actions()
    }

    sealed class States {
        data class SearchResult(val data: PagingData<Character>) : States()
    }
}