package github.bed72.bedapp.presentation.detail

import javax.inject.Inject

import androidx.lifecycle.ViewModel

import dagger.hilt.android.lifecycle.HiltViewModel

import github.bed72.core.usecase.AddFavoriteUseCase
import github.bed72.core.usecase.CheckFavoriteUseCase
import github.bed72.core.usecase.RemoveFavoriteUseCase
import github.bed72.core.usecase.base.CoroutinesDispatchers
import github.bed72.core.usecase.GetCharacterCategoriesUseCase
import github.bed72.bedapp.presentation.detail.redux.LoadLiveData
import github.bed72.bedapp.presentation.detail.redux.FavoritesLiveData

@HiltViewModel
class DetailViewModel @Inject constructor(
    addFavoriteUseCase: AddFavoriteUseCase,
    coroutineDispatcher: CoroutinesDispatchers,
    checkFavoriteUseCase: CheckFavoriteUseCase,
    removeFavoriteUseCase: RemoveFavoriteUseCase,
    getCharacterCategoriesUseCase: GetCharacterCategoriesUseCase
) : ViewModel() {

    val initial = LoadLiveData(
        coroutineDispatcher.main(),
        getCharacterCategoriesUseCase
    )

    val favorite = FavoritesLiveData(
        coroutineDispatcher.main(),
        addFavoriteUseCase,
        checkFavoriteUseCase,
        removeFavoriteUseCase
    )
}