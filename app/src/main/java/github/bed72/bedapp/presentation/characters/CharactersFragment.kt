package github.bed72.bedapp.presentation.characters

import android.os.Bundle
import android.view.View
import dagger.hilt.android.AndroidEntryPoint


import javax.inject.Inject
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest

import androidx.paging.LoadState
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras

import github.bed72.core.domain.model.Character
import github.bed72.bedapp.presentation.base.BaseFragment
import github.bed72.bedapp.databinding.FragmentCharactersBinding
import github.bed72.bedapp.presentation.detail.args.DetailViewArg
import github.bed72.bedapp.framework.imageloader.usecase.ImageLoader
import github.bed72.bedapp.presentation.characters.adapters.CharactersAdapter
import github.bed72.bedapp.presentation.characters.adapters.CharactersLoadStateAdapter
import github.bed72.bedapp.presentation.characters.CharactersViewModel.States.SearchResult

@AndroidEntryPoint
class CharactersFragment : BaseFragment<FragmentCharactersBinding>() {

    @Inject
    lateinit var imageLoader: ImageLoader

    private val viewModel: CharactersViewModel by viewModels()

    private val charactersAdapter: CharactersAdapter by lazy { setLazyAdapter() }

    override fun getViewBinding() = FragmentCharactersBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        observeInitialLoadState()
        handleCharactersPagingData()
    }

    private fun handleCharactersPagingData() {
        with (viewModel) {
            state.observe(viewLifecycleOwner) { states ->
                when (states) {
                    is SearchResult ->
                        charactersAdapter.submitData(viewLifecycleOwner.lifecycle, states.data)
                }
            }

            search()
        }
    }

    private fun setLazyAdapter() = CharactersAdapter(imageLoader) { character, view ->
        handleNavigation(view, character)
    }

    private fun initAdapter() {
        postponeEnterTransition()
        with(binding.recyclerCharacters) {
            setHasFixedSize(true)
            adapter = charactersAdapter.withLoadStateFooter(
                footer = CharactersLoadStateAdapter(
                    charactersAdapter::retry
                )
            )
            viewTreeObserver.addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }
        }
    }

    private fun handleNavigation(view: View, character: Character) {
        val extras = FragmentNavigatorExtras(view to character.name)

        val directions = CharactersFragmentDirections.actionCharactersFragmentToDetailFragment(
            character.name,
            DetailViewArg(
                characterId = character.id,
                name = character.name,
                imageUrl = character.imageUrl
            )
        )

        findNavController().navigate(directions, extras)
    }

    private fun observeInitialLoadState() {
        lifecycleScope.launch {
            charactersAdapter.loadStateFlow.collectLatest { loadState ->
                 binding.flipperCharacters.displayedChild = when (loadState.refresh) {
                    is LoadState.Loading -> {
                        setShimmerVisibility(true)

                        FLIPPER_LOADING
                    }
                     is LoadState.NotLoading -> {
                         setShimmerVisibility(false)

                         FLIPPER_SUCCESS
                     }
                     is LoadState.Error -> {
                         setShimmerVisibility(false)

                         binding.includeViewCharactersErrorState.buttonRetry.setOnClickListener {
                             charactersAdapter.retry()
                         }

                         FLIPPER_ERROR
                     }
                }
            }
        }
    }

    private fun setShimmerVisibility(visibility: Boolean) {
        binding.includeViewCharactersLoadingState.shimmerCharacters.run {
            isVisible = visibility

            if (visibility) startShimmer() else startShimmer()
        }
    }

    companion object {
        private const val FLIPPER_LOADING = 0
        private const val FLIPPER_SUCCESS = 1
        private const val FLIPPER_ERROR = 2
    }
}