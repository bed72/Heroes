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
import github.bed72.bedapp.R
import github.bed72.bedapp.presentation.detail.entities.DetailChildViewEntity
import github.bed72.bedapp.presentation.detail.entities.DetailParentViewEntity
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
                is ResultStatus.Success -> handleViewEntity(status.data)
            }
        }
    }

    private fun handleViewEntity(data: List<Comic>): UiState.Success {
        val detailChildList = data.map { DetailChildViewEntity(it.id, it.imageUrl) }
        val detailParentList = listOf(
            DetailParentViewEntity(
                detailChildViewEntity = detailChildList,
                categoryStringResId = R.string.details_comics_category
            )
        )

        return UiState.Success(detailParentList)
    }

    sealed class UiState {
        object Error : UiState()
        object Loading : UiState()
        data class Success(val detailParentList: List<DetailParentViewEntity>) : UiState()
    }
}