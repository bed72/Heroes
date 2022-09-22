package github.bed72.bedapp.presentation.sort

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import dagger.hilt.android.lifecycle.HiltViewModel
import github.bed72.bedapp.presentation.extensions.watchStatus
import github.bed72.bedapp.presentation.sort.SortViewModel.Actions.ApplySorting
import github.bed72.bedapp.presentation.sort.SortViewModel.Actions.GetStoredSorting
import github.bed72.bedapp.presentation.sort.SortViewModel.States.Apply.Error
import github.bed72.bedapp.presentation.sort.SortViewModel.States.Apply.Loading
import github.bed72.bedapp.presentation.sort.SortViewModel.States.Apply.Success
import github.bed72.bedapp.presentation.sort.SortViewModel.States.SortingResult
import github.bed72.core.usecase.GetCharactersSortingUseCase
import github.bed72.core.usecase.SaveCharactersSortingUseCase
import github.bed72.core.usecase.SaveCharactersSortingUseCase.SaveCharactersSortingParams
import github.bed72.core.usecase.base.CoroutinesDispatchers
import javax.inject.Inject

@HiltViewModel
class SortViewModel @Inject constructor(
    private val coroutineDispatcher: CoroutinesDispatchers,
    private val getCharactersSortingUseCase: GetCharactersSortingUseCase,
    private val saveCharactersSortingUseCase: SaveCharactersSortingUseCase
) : ViewModel() {

    private val action = MutableLiveData<Actions>()
    val state: LiveData<States> = action.switchMap { actions ->
        liveData(coroutineDispatcher.main()) {
            when (actions) {
                GetStoredSorting -> {
                    getCharactersSortingUseCase().collect { sortingPair ->
                        emit(SortingResult(sortingPair))
                    }
                }
                is ApplySorting -> {
                    val (orderBy, order) = actions

                    saveCharactersSortingUseCase(
                        SaveCharactersSortingParams(orderBy to order)
                    ).watchStatus(
                        error = { emit(Error) },
                        loading = { emit(Loading) },
                        success = { emit(Success) }
                    )
                }
            }
        }
    }

    init {
        action.value = GetStoredSorting
    }

    fun applySorting(orderBy: String, order: String) {
        action.value = ApplySorting(orderBy, order)
    }

    sealed class States {
        data class SortingResult(val storedSorting: Pair<String, String>) : States()

        sealed class Apply : States() {
            object Error : Apply()
            object Loading : Apply()
            object Success : Apply()
        }
    }

    sealed class Actions {
        object GetStoredSorting : Actions()
        data class ApplySorting(val orderBy: String, val order: String) : Actions()
    }
}
