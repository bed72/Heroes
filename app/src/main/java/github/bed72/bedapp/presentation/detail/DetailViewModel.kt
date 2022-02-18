package github.bed72.bedapp.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import github.bed72.core.domain.model.Comic
import github.bed72.core.usecase.GetComicsUseCase
import github.bed72.core.usecase.base.ResultStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import github.bed72.core.usecase.GetComicsUseCase.GetComicsParams

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getComicsUseCase: GetComicsUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> get() = _uiState

    fun getComics(characterId: Int) {
        getComicsUseCase(GetComicsParams(characterId)).watchStatus()
    }

    private fun Flow<ResultStatus<List<Comic>>>.watchStatus() = viewModelScope.launch {
        collect { status ->
            _uiState.value = when (status) {
                ResultStatus.Loading -> UiState.Loading
                is ResultStatus.Error -> UiState.Error // use 'is' when data class
                is ResultStatus.Success -> UiState.Success(status.data)
            }
        }
    }

    sealed class UiState {
        object Error: UiState()
        object Loading : UiState()
        data class Success(val comics: List<Comic>) : UiState()
    }
}