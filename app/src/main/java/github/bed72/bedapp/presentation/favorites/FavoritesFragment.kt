package github.bed72.bedapp.presentation.favorites

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import github.bed72.bedapp.databinding.FragmentFavoritesBinding
import github.bed72.bedapp.framework.imageloader.usecase.ImageLoader
import github.bed72.bedapp.presentation.common.fragment.BaseFragment
import github.bed72.bedapp.presentation.common.recycler.getGenericAdapterOf
import github.bed72.bedapp.presentation.favorites.FavoritesViewModel.States.ShowEmpty
import github.bed72.bedapp.presentation.favorites.FavoritesViewModel.States.ShowFavorites
import github.bed72.bedapp.presentation.favorites.viewholders.FavoritesViewHolder
import javax.inject.Inject

@AndroidEntryPoint
class FavoritesFragment : BaseFragment<FragmentFavoritesBinding>() {

    @Inject
    lateinit var imageLoader: ImageLoader

    private val viewModel: FavoritesViewModel by viewModels()

    private val favoriteAdapter by lazy {
        getGenericAdapterOf { parent ->
            FavoritesViewHolder.create(parent, imageLoader)
        }
    }

    override fun getViewBinding() = FragmentFavoritesBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        observeFavoritesState()
    }

    private fun observeFavoritesState() {
        with(viewModel) {
            states.observe(viewLifecycleOwner) { states ->
                binding.flipperFavorites.displayedChild = when (states) {
                    ShowEmpty -> {
                        favoriteAdapter.submitList(emptyList())

                        FLIPPER_EMPTY
                    }
                    is ShowFavorites -> {
                        favoriteAdapter.submitList(states.favorites)

                        FLIPPER_FAVORITES
                    }
                }
            }

            getAll()
        }
    }

    private fun initAdapter() {
        binding.recyclerFavorites.run {
            setHasFixedSize(true)
            adapter = favoriteAdapter
        }
    }

    companion object {
        private const val FLIPPER_FAVORITES = 0
        private const val FLIPPER_EMPTY = 1
    }
}
