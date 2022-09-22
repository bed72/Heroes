package github.bed72.bedapp.presentation.characters

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import github.bed72.bedapp.R
import github.bed72.bedapp.databinding.FragmentCharactersBinding
import github.bed72.bedapp.framework.imageloader.usecase.ImageLoader
import github.bed72.bedapp.presentation.characters.CharactersViewModel.States.SearchResult
import github.bed72.bedapp.presentation.characters.adapters.CharactersAdapter
import github.bed72.bedapp.presentation.characters.adapters.CharactersLoadMoreStateAdapter
import github.bed72.bedapp.presentation.characters.adapters.CharactersRefreshStateAdapter
import github.bed72.bedapp.presentation.common.fragment.BaseFragment
import github.bed72.bedapp.presentation.detail.args.DetailViewArg
import github.bed72.bedapp.presentation.sort.SortFragment
import github.bed72.core.domain.model.Character
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CharactersFragment :
    BaseFragment<FragmentCharactersBinding>(),
    SearchView.OnQueryTextListener,
    MenuItem.OnActionExpandListener {

    @Inject
    lateinit var imageLoader: ImageLoader

    private lateinit var searchView: SearchView

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
        observerSortingData()
        observeInitialLoadState()
        handleCharactersPagingData()
    }

    override fun onQueryTextSubmit(query: String?): Boolean =
        query?.let { value ->
            with(viewModel) {
                currentSearchQuery = value

                search()
            }

            true
        } ?: false

    override fun onQueryTextChange(newText: String?): Boolean = true

    override fun onDestroyView() {
        super.onDestroyView()

        searchView.setOnQueryTextListener(null)
    }

    override fun onMenuItemActionExpand(item: MenuItem): Boolean = true

    override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
        with(viewModel) {
            close()
            search()
        }

        return true
    }

    private fun initMenu() {
        val menuSort: MenuHost = requireActivity()

        menuSort.addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.characters_menu_items, menu)

                    initSearchBar(menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.menu_sort -> {
                            findNavController().navigate(R.id.action_characters_fragment_to_sortFragment)
                            true
                        } else -> false
                    }
                }
            },
            viewLifecycleOwner, Lifecycle.State.RESUMED
        )
    }

    private fun initSearchBar(menu: Menu) {
        val searchItem = menu.findItem(R.id.menu_search)
        searchView = searchItem.actionView as SearchView

        searchItem.setOnActionExpandListener(this)

        observerStateSearchBar(searchItem)

        searchView.run {
            isSubmitButtonEnabled = true
            setOnQueryTextListener(this@CharactersFragment)
        }
    }

    private fun observerStateSearchBar(searchItem: MenuItem) {
        if (viewModel.currentSearchQuery.isNotEmpty()) {
            searchItem.expandActionView()
            searchView.setQuery(viewModel.currentSearchQuery, false)
        }
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
                    loadState.mediator?.refresh is LoadState.Error &&
                        charactersAdapter.itemCount == 0 -> setScreenError()
                    // Source -> Local
                    loadState.source.refresh is LoadState.NotLoading ||
                        loadState.mediator?.refresh is LoadState.NotLoading -> {
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

    private fun observerSortingData() {
        val navBackStackEntry = findNavController().getBackStackEntry(R.id.characters_fragment)
        val observer = LifecycleEventObserver { _, event ->
            val isSortingApplied = navBackStackEntry.savedStateHandle.contains(
                SortFragment.SORTING_APPLIED_BASK_STACK_KEY
            )

            if (event == Lifecycle.Event.ON_RESUME && isSortingApplied) {
                // Call API
                viewModel.sort()

                // Clear State
                navBackStackEntry.savedStateHandle.remove<Boolean>(
                    SortFragment.SORTING_APPLIED_BASK_STACK_KEY
                )
            }
        }

        navBackStackEntry.lifecycle.addObserver(observer)

        viewLifecycleOwner.lifecycle.addObserver(
            LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_DESTROY)
                    navBackStackEntry.lifecycle.removeObserver(observer)
            }
        )
    }

    companion object {
        private const val FLIPPER_LOADING = 0
        private const val FLIPPER_SUCCESS = 1
        private const val FLIPPER_ERROR = 2
    }

}
