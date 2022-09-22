package github.bed72.bedapp.presentation.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import dagger.hilt.android.lifecycle.HiltViewModel
import github.bed72.bedapp.presentation.favorites.FavoritesViewModel.Actions.GetAll
import github.bed72.bedapp.presentation.favorites.FavoritesViewModel.States.ShowEmpty
import github.bed72.bedapp.presentation.favorites.FavoritesViewModel.States.ShowFavorites
import github.bed72.bedapp.presentation.favorites.data.FavoriteItem
import github.bed72.core.domain.model.Character
import github.bed72.core.usecase.GetFavoritesUseCase
import github.bed72.core.usecase.base.CoroutinesDispatchers
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val dispatchers: CoroutinesDispatchers,
    private val getFavoritesUseCase: GetFavoritesUseCase
) : ViewModel() {

    private val action = MutableLiveData<Actions>()
    val states: LiveData<States> = action.switchMap { action ->
        liveData(dispatchers.main()) {
            when (action) {
                is GetAll -> getFavoritesUseCase()
                    .catch { emit(ShowEmpty) }
                    .collect { characters ->
                        val items = favoriteItem(characters)
                        val states = if (items.isEmpty()) ShowEmpty else ShowFavorites(items)

                        emit(states)
                    }
            }
        }
    }

    fun getAll() {
        action.value = GetAll
    }

    private fun favoriteItem(characters: List<Character>) = characters.map { character ->
        FavoriteItem(
            character.name,
            character.imageUrl,
            character.id
        )
    }

    sealed class Actions {
        object GetAll : Actions()
    }

    sealed class States {
        object ShowEmpty : States()
        data class ShowFavorites(val favorites: List<FavoriteItem>) : States()
    }
}
