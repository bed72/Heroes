package github.bed72.bedapp.presentation.detail

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import github.bed72.bedapp.databinding.FragmentDetailBinding
import github.bed72.bedapp.framework.imageloader.usecase.ImageLoader
import github.bed72.bedapp.presentation.common.fragment.BaseFragment
import github.bed72.bedapp.presentation.detail.adapters.DetailParentAdapter
import github.bed72.bedapp.presentation.detail.args.DetailViewArg
import github.bed72.bedapp.presentation.detail.redux.FavoritesLiveData
import github.bed72.bedapp.presentation.detail.redux.LoadLiveData
import github.bed72.bedapp.presentation.extensions.showShortToast
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>() {

    @Inject
    lateinit var imageLoader: ImageLoader

    private val args by navArgs<DetailFragmentArgs>()
    private val viewModel: DetailViewModel by viewModels()

    override fun getViewBinding() = FragmentDetailBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHeaderImageAndStSharedElementTransitionOnEnter(args.detailViewArg)

        observeFavoritesState(args.detailViewArg)
        observeCategoriesState(args.detailViewArg)
    }

    // Define a animação da transição como "move" a imagem do Personagem para o Header
    private fun setHeaderImageAndStSharedElementTransitionOnEnter(detailViewArgs: DetailViewArg) {
        binding.imageCharacter.run {
            transitionName = detailViewArgs.name
            imageLoader.load(this, detailViewArgs.imageUrl)
        }

        TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move).apply {
                sharedElementEnterTransition = this
            }
    }

    private fun observeCategoriesState(detailViewArg: DetailViewArg) {
        with(viewModel.initial) {
            load(detailViewArg.characterId)

            state.observe(viewLifecycleOwner) { states ->
                binding.flipperDetail.displayedChild = when (states) {
                    LoadLiveData.States.Empty -> FLIPPER_CATEGORIES_EMPTY
                    LoadLiveData.States.Loading -> FLIPPER_CATEGORIES_LOADING
                    LoadLiveData.States.Error -> {
                        binding.includeViewDetailErrorState.buttonRetry.setOnClickListener {
                            load(detailViewArg.characterId)
                        }

                        FLIPPER_CATEGORIES_ERROR
                    }
                    is LoadLiveData.States.Success -> {
                        binding.recyclerParentDetail.run {
                            setHasFixedSize(true)
                            adapter = DetailParentAdapter(imageLoader, states.detailParentList)
                        }

                        FLIPPER_CATEGORIES_SUCCESS
                    }
                }
            }
        }
    }

    private fun observeFavoritesState(detailViewArg: DetailViewArg) {
        viewModel.favorite.run {
            checkFavorite(detailViewArg.characterId)

            binding.imageFavoriteIcon.setOnClickListener {
                updateFavorite(detailViewArg)
            }

            state.observe(viewLifecycleOwner) { states ->
                binding.flipperFavorite.displayedChild = when (states) {
                    FavoritesLiveData.States.Loading -> FLIPPER_FAVORITE_LOADING
                    is FavoritesLiveData.States.Icon -> {
                        binding.imageFavoriteIcon.setImageResource(states.icon)

                        FLIPPER_FAVORITE_SHOW_ICON
                    }
                    is FavoritesLiveData.States.Error -> {
                        showShortToast(states.message)

                        FLIPPER_FAVORITE_SHOW_ICON
                    }
                }
            }
        }
    }

    companion object {
        private const val FLIPPER_CATEGORIES_LOADING = 0
        private const val FLIPPER_CATEGORIES_SUCCESS = 1
        private const val FLIPPER_CATEGORIES_ERROR = 2
        private const val FLIPPER_CATEGORIES_EMPTY = 3
        private const val FLIPPER_FAVORITE_SHOW_ICON = 0
        private const val FLIPPER_FAVORITE_LOADING = 1
    }
}
