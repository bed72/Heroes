package github.bed72.bedapp.presentation.detail.redux

import androidx.lifecycle.liveData
import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import androidx.annotation.StringRes
import androidx.annotation.DrawableRes
import androidx.lifecycle.MutableLiveData

import kotlin.coroutines.CoroutineContext

import github.bed72.bedapp.R
import github.bed72.core.usecase.AddFavoriteUseCase
import github.bed72.core.usecase.CheckFavoriteUseCase
import github.bed72.bedapp.presentation.extensions.watchStatus
import github.bed72.bedapp.presentation.detail.args.DetailViewArg
import github.bed72.core.usecase.AddFavoriteUseCase.AddFavoriteParams
import github.bed72.core.usecase.CheckFavoriteUseCase.CheckFavoriteParams
import github.bed72.bedapp.presentation.detail.redux.FavoritesLiveData.States.Icon
import github.bed72.bedapp.presentation.detail.redux.FavoritesLiveData.States.Error
import github.bed72.bedapp.presentation.detail.redux.FavoritesLiveData.States.Loading
import github.bed72.bedapp.presentation.detail.redux.FavoritesLiveData.Actions.CheckFavorite
import github.bed72.bedapp.presentation.detail.redux.FavoritesLiveData.Actions.Favorite

class FavoritesLiveData(
    private val coroutineContext: CoroutineContext,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val checkFavoriteUseCase: CheckFavoriteUseCase
) {
    private val action = MutableLiveData<Actions>()
    val state: LiveData<States> = action.switchMap { actions ->
        liveData(coroutineContext) {
            when (actions) {
                is CheckFavorite -> {
                    checkFavoriteUseCase(
                        CheckFavoriteParams(actions.characterId)
                    ).watchStatus(
                        error = {emit(Icon(R.drawable.ic_favorite_error))},
                        success = { isFavorite ->
                            emit(Icon(type(isFavorite)))
                        }
                    )
                }
                is Favorite -> {
                    actions.detailViewArg.run {
                        addFavoriteUseCase(
                            AddFavoriteParams(characterId, name, imageUrl)
                        ).watchStatus(
                            loading = { emit(Loading) },
                            error = { emit(Error(R.string.error_favorite_data)) },
                            success = { emit(Icon(R.drawable.ic_favorite_checked)) }
                        )
                    }
                }
            }
        }
    }

    fun checkFavorite(characterId: Int) {
        action.value = CheckFavorite(characterId)
    }

    fun favorite(detailViewArg: DetailViewArg) {
        action.value = Favorite(detailViewArg)
    }

    private fun type(isFavorite: Boolean) =
        if (isFavorite) R.drawable.ic_favorite_checked else R.drawable.ic_favorite_unchecked

    sealed class Actions {
        data class CheckFavorite(val characterId: Int) : Actions()
        data class Favorite(val detailViewArg: DetailViewArg) : Actions()
    }

    sealed class States {
        object Loading : States()
        data class Icon(@DrawableRes val icon: Int) : States()
        data class Error(@StringRes val message: Int) : States()
    }
}