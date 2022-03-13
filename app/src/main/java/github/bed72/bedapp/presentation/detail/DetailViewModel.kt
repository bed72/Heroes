package github.bed72.bedapp.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import github.bed72.core.domain.model.Comic
import github.bed72.core.usecase.GetCharacterCategoriesUseCase
import github.bed72.core.usecase.base.ResultStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import github.bed72.bedapp.R
import github.bed72.bedapp.presentation.detail.entities.DetailChildViewEntity
import github.bed72.bedapp.presentation.detail.entities.DetailParentViewEntity
import github.bed72.core.domain.model.Event
import github.bed72.core.usecase.GetCharacterCategoriesUseCase.GetCharacterCategoriesParams

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getCharacterCategoriesUseCase: GetCharacterCategoriesUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> get() = _uiState

    fun getCharacterCategories(characterId: Int) {
        getCharacterCategoriesUseCase(GetCharacterCategoriesParams(characterId)).watchStatus()
    }

    private fun Flow<ResultStatus<Pair<List<Comic>, List<Event>>>>.watchStatus() =
        viewModelScope.launch {
            collect { status ->
                _uiState.value = when (status) {
                    ResultStatus.Loading -> UiState.Loading
                    is ResultStatus.Error -> UiState.Error // use 'is' when data class
                    is ResultStatus.Success -> handleViewEntity(status.data)
                }
            }
    }

    private fun handleViewEntity(data: Pair<List<Comic>, List<Event>>): UiState {
        val parentList = verifyIfExistDataInComicAndEvent(data.first, data.second)

        return if (parentList.isNotEmpty()) UiState.Success(parentList) else UiState.Empty
    }

    private fun verifyIfExistDataInComicAndEvent(
        comic: List<Comic>,
        event: List<Event>
    ): MutableList<DetailParentViewEntity> {
        val parentList = mutableListOf<DetailParentViewEntity>()

        if (comic.isNotEmpty())
            comic.map { child  ->
                DetailChildViewEntity(child.id, child.imageUrl)
            }.also { child ->
                parentList.add(
                    DetailParentViewEntity(R.string.details_comics_category, child)
                )
            }

        if (event.isNotEmpty())
            event.map { child  ->
                DetailChildViewEntity(child.id, child.imageUrl)
            }.also { child ->
                parentList.add(
                    DetailParentViewEntity(R.string.details_events_category, child)
                )
            }

        return parentList
    }

    sealed class UiState {
        object Empty: UiState()
        object Error : UiState()
        object Loading : UiState()
        data class Success(val detailParentList: List<DetailParentViewEntity>) : UiState()
    }
}