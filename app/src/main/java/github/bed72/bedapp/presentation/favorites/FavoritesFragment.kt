package github.bed72.bedapp.presentation.favorites

import android.os.Bundle
import android.view.View
import dagger.hilt.android.AndroidEntryPoint

import javax.inject.Inject

import github.bed72.bedapp.databinding.FragmentFavoritesBinding
import github.bed72.bedapp.framework.imageloader.usecase.ImageLoader
import github.bed72.bedapp.presentation.common.fragment.BaseFragment
import github.bed72.bedapp.presentation.common.recycler.getGenericAdapterOf
import github.bed72.bedapp.presentation.favorites.viewholders.FavoritesViewHolder

@AndroidEntryPoint
class FavoritesFragment : BaseFragment<FragmentFavoritesBinding>() {

    @Inject
    lateinit var imageLoader: ImageLoader

    private val favoriteAdapter by lazy {
        getGenericAdapterOf { parent ->
            FavoritesViewHolder.create(parent, imageLoader)
        }
    }

    override fun getViewBinding() = FragmentFavoritesBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
    }

    private fun initAdapter() {
        binding.recyclerFavorites.run {
            setHasFixedSize(true)
            adapter = favoriteAdapter
        }
    }

}