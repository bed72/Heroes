package github.bed72.bedapp.presentation.characters

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider

import dagger.hilt.android.AndroidEntryPoint

import javax.inject.Inject
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest

import androidx.paging.LoadState
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.paging.CombinedLoadStates
import github.bed72.bedapp.R

import github.bed72.core.domain.model.Character
import github.bed72.bedapp.databinding.FragmentCharactersBinding
import github.bed72.bedapp.presentation.detail.args.DetailViewArg
import github.bed72.bedapp.presentation.common.fragment.BaseFragment
import github.bed72.bedapp.framework.imageloader.usecase.ImageLoader
import github.bed72.bedapp.presentation.characters.adapters.CharactersAdapter
import github.bed72.bedapp.presentation.characters.adapters.CharactersLoadMoreStateAdapter
import github.bed72.bedapp.presentation.characters.CharactersViewModel.States.SearchResult
import github.bed72.bedapp.presentation.characters.adapters.CharactersRefreshStateAdapter

@AndroidEntryPoint
class CharactersFragment : BaseFragment<FragmentCharactersBinding>() {

    @Inject
    lateinit var imageLoader: ImageLoader

    private val viewModel: CharactersViewModel by viewModels()

    private val charactersAdapter: CharactersAdapter by lazy { setLazyAdapter() }

    private val headerAdapter: CharactersRefreshStateAdapter by lazy {
        CharactersRefreshStateAdapter(
            charactersAdapter::retry
        )
    }

    override fun getViewBinding() = FragmentCharactersBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMenu()
        initAdapter()
        observeInitialLoadState()
        handleCharactersPagingData()
    }

    private fun initMenu() {
        val menuSort: MenuHost = requireActivity()

        menuSort.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.characters_menu_items, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when(menuItem.itemId) {
                    R.id.menu_sort -> {
                        findNavController().navigate(R.id.action_characters_fragment_to_sortFragment)
                        true
                    } else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun initAdapter() {
        postponeEnterTransition()
        with(binding.recyclerCharacters) {
            setHasFixedSize(true)
            adapter = charactersAdapter.withLoadStateHeaderAndFooter(
                header = headerAdapter,
                footer = CharactersLoadMoreStateAdapter(
                    charactersAdapter::retry
                )
            )
            viewTreeObserver.addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }
        }
    }

    private fun observeInitialLoadState() {
        lifecycleScope.launch {
            charactersAdapter.loadStateFlow.collectLatest { loadState ->
                showInformationOffline(loadState)

                binding.flipperCharacters.displayedChild = when {
                    // Mediator -> remote
                    loadState.mediator?.refresh is LoadState.Loading -> {
                        setShimmerVisibility(true)
                        FLIPPER_LOADING
                    }
                    loadState.mediator?.refresh is LoadState.Error
                            && charactersAdapter.itemCount == 0 -> setScreenError()
                    // Source -> Local
                    loadState.source.refresh is LoadState.NotLoading
                            || loadState.mediator?.refresh is LoadState.NotLoading -> {
                        setShimmerVisibility(false)
                        FLIPPER_SUCCESS
                    }
                    else -> setScreenError()
                }
            }
        }
    }

    private fun handleCharactersPagingData() {
        with(viewModel) {
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

    private fun showInformationOffline(loadState: CombinedLoadStates) {
        headerAdapter.loadState = loadState.mediator?.refresh?.takeIf { state ->
            // Retorne se
            state is LoadState.Error && charactersAdapter.itemCount > 0
        } ?: loadState.prepend
    }

    private fun setScreenError(): Int {
        setShimmerVisibility(false)

        binding.includeViewCharactersErrorState.buttonRetry.setOnClickListener {
            charactersAdapter.retry()
        }

        return FLIPPER_ERROR
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