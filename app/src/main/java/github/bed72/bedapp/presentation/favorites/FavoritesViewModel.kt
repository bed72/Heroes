package github.bed72.bedapp.presentation.favorites

import javax.inject.Inject

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

import github.bed72.core.usecase.GetFavoritesUseCase
import github.bed72.core.usecase.base.CoroutinesDispatchers

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val dispatchers: CoroutinesDispatchers,
    private val getFavoritesUseCase: GetFavoritesUseCase
) : ViewModel() {
}