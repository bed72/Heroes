package github.bed72.bedapp.presentation.characters

import android.os.Bundle
import android.view.View
import javax.inject.Inject
import androidx.paging.LoadState
import kotlinx.coroutines.launch
import androidx.lifecycle.Lifecycle
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import github.bed72.core.domain.model.Character
import androidx.navigation.fragment.findNavController
import github.bed72.bedapp.presentation.base.BaseFragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import github.bed72.bedapp.databinding.FragmentCharactersBinding
import github.bed72.bedapp.presentation.detail.args.DetailViewArg
import github.bed72.bedapp.framework.imageloader.usecase.ImageLoader
import github.bed72.bedapp.presentation.characters.adapters.CharactersAdapter
import github.bed72.bedapp.presentation.characters.adapters.CharactersLoadStateAdapter

@AndroidEntryPoint
class CharactersFragment : BaseFragment<FragmentCharactersBinding>() {

    @Inject
    lateinit var imageLoader: ImageLoader

    private val viewModel: CharactersViewModel by viewModels()
    private lateinit var charactersAdapter: CharactersAdapter

    override fun getViewBinding() = FragmentCharactersBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCharactersAdapter()
        observeInitialLoadState()
        handleCharactersPagingData()
    }

    private fun handleCharactersPagingData() {
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.charactersPagingData("").collect { pagingData ->
                    charactersAdapter.submitData(pagingData)
                }
            }
        }
    }

    private fun initCharactersAdapter() {
        charactersAdapter = CharactersAdapter(imageLoader) { character, view ->
            handleNavigation(view, character)
        }

        with(binding.recyclerCharacters) {
            setHasFixedSize(true)
            adapter = charactersAdapter.withLoadStateFooter(
                footer = CharactersLoadStateAdapter(
                    // Passed lambda function
                    charactersAdapter::retry
                )
            )
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