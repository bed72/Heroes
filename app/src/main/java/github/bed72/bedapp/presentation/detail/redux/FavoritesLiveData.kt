package github.bed72.bedapp.presentation.detail.redux

import androidx.lifecycle.liveData
import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import androidx.annotation.StringRes
import androidx.annotation.DrawableRes
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveDataScope
import androidx.lifecycle.MutableLiveData

import kotlin.coroutines.CoroutineContext

import github.bed72.bedapp.R
import github.bed72.core.usecase.AddFavoriteUseCase
import github.bed72.core.usecase.CheckFavoriteUseCase
import github.bed72.core.usecase.RemoveFavoriteUseCase
import github.bed72.bedapp.presentation.extensions.watchStatus
import github.bed72.bedapp.presentation.detail.args.DetailViewArg
import github.bed72.core.usecase.AddFavoriteUseCase.AddFavoriteParams
import github.bed72.core.usecase.CheckFavoriteUseCase.CheckFavoriteParams
import github.bed72.core.usecase.RemoveFavoriteUseCase.RemoveFavoriteParams
import github.bed72.bedapp.presentation.detail.redux.FavoritesLiveData.States.Icon
import github.bed72.bedapp.presentation.detail.redux.FavoritesLiveData.States.Error
import github.bed72.bedapp.presentation.detail.redux.FavoritesLiveData.States.Loading
import github.bed72.bedapp.presentation.detail.redux.FavoritesLiveData.Actions.AddFavorite
import github.bed72.bedapp.presentation.detail.redux.FavoritesLiveData.Actions.CheckFavorite
import github.bed72.bedapp.presentation.detail.redux.FavoritesLiveData.Actions.RemoveFavorite

class FavoritesLiveData(
    private val coroutineContext: CoroutineContext,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val checkFavoriteUseCase: CheckFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase
) {
    private val action = MutableLiveData<Actions>()

    @set:VisibleForTesting
    var currentFavoriteIcon = R.drawable.ic_favorite_unchecked

    val state: LiveData<States> = action.switchMap { actions ->
        liveData(coroutineContext) {
            when (actions) {
                is CheckFavorite -> {
                    checkFavoriteUseCase(
                        CheckFavoriteParams(actions.characterId)
                    ).watchStatus(
                        error = {
                            currentFavoriteIcon = R.drawable.ic_favorite_error

                            emitFavoriteIcon()
                        },
                        success = { isFavorite ->
                            if (isFavorite) currentFavoriteIcon = R.drawable.ic_favorite_checked

                            emitFavoriteIcon()
                        }
                    )
                }
                is AddFavorite -> {
                    actions.detailViewArg.run {
                        addFavoriteUseCase(
                            AddFavoriteParams(characterId, name, imageUrl)
                        ).watchStatus(
                            loading = { emit(Loading) },
                            error = { emit(Error(R.string.error_add_favorite_data)) },
                            success = {
                                currentFavoriteIcon = R.drawable.ic_favorite_checked

                                emitFavoriteIcon()
                            }
                        )
                    }
                }
                is RemoveFavorite -> {
                    actions.detailViewArg.run {
                        removeFavoriteUseCase(
                            RemoveFavoriteParams(characterId, name, imageUrl)
                        ).watchStatus(
                            loading = { emit(Loading) },
                            error = { emit(Error(R.string.error_remove_favorite_data)) },
                            success = {
                                currentFavoriteIcon = R.drawable.ic_favorite_unchecked

                                emitFavoriteIcon()
                            }
                        )
                    }
                }
            }
        }
    }

    fun checkFavorite(characterId: Int) {
        action.value = CheckFavorite(characterId)
    }

    fun updateFavorite(detailViewArg: DetailViewArg) {
        action.value = if (currentFavoriteIcon == R.drawable.ic_favorite_unchecked)
            AddFavorite(detailViewArg)
        else RemoveFavorite(detailViewArg)
    }

    private suspend fun LiveDataScope<States>.emitFavoriteIcon() {
        emit(Icon(currentFavoriteIcon))
    }

    sealed class Actions {
        data class CheckFavorite(val characterId: Int) : Actions()
        data class AddFavorite(val detailViewArg: DetailViewArg) : Actions()
        data class RemoveFavorite(val detailViewArg: DetailViewArg) : Actions()
    }

    sealed class States {
        object Loading : States()
        data class Icon(@DrawableRes val icon: Int) : States()
        data class Error(@StringRes val message: Int) : States()
    }
}