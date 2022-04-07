package github.bed72.bedapp.presentation.detail


import javax.inject.Inject
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import github.bed72.core.usecase.AddFavoriteUseCase
import github.bed72.core.usecase.base.CoroutinesDispatchers
import github.bed72.core.usecase.GetCharacterCategoriesUseCase
import github.bed72.bedapp.presentation.detail.redux.FavoritesLiveData
import github.bed72.bedapp.presentation.detail.redux.LoadLiveData

@HiltViewModel
class DetailViewModel @Inject constructor(
    addFavoriteUseCase: AddFavoriteUseCase,
    coroutineDispatcher: CoroutinesDispatchers,
    getCharacterCategoriesUseCase: GetCharacterCategoriesUseCase
) : ViewModel() {

    val initial = LoadLiveData(
        coroutineDispatcher.main(),
        getCharacterCategoriesUseCase
    )

    val favorite = FavoritesLiveData(
        coroutineDispatcher.main(),
        addFavoriteUseCase
    )

    init {
        favorite.setDefault()
    }
}