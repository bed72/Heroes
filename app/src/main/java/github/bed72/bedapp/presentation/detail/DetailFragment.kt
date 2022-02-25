package github.bed72.bedapp.presentation.detail

import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import github.bed72.bedapp.R
import github.bed72.bedapp.databinding.FragmentDetailBinding
import github.bed72.bedapp.framework.imageloader.usecase.ImageLoader
import github.bed72.bedapp.presentation.detail.adapters.DetailParentAdapter
import github.bed72.bedapp.presentation.detail.entities.DetailParentViewEntity
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment : Fragment() {

    @Inject
    lateinit var imageLoader: ImageLoader

    private var _binding: FragmentDetailBinding? = null
    private val binding: FragmentDetailBinding get() = _binding!!

    private val args by navArgs<DetailFragmentArgs>()

    private val viewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentDetailBinding.inflate(
        inflater,
        container,
        false
    ).apply {
        _binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDetails()
    }

    private fun setupDetails() {
        val detailViewArgs = args.detailViewArg
        binding.imageCharacter.run {
            transitionName = detailViewArgs.name
            imageLoader.load(this, detailViewArgs.imageUrl)
        }

        setSharedElementTransitionOnEnter()
        observeInitialLoadState(detailViewArgs.characterId)
    }

    // Define a animação da transição como "move"
    private fun setSharedElementTransitionOnEnter() {
        TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move).apply {
                sharedElementEnterTransition = this
            }
    }

    private fun observeInitialLoadState(characterId: Int) {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                DetailViewModel.UiState.Error -> { }
                DetailViewModel.UiState.Loading -> { }
                is DetailViewModel.UiState.Success -> initDetailAdapter(uiState.detailParentList)
            }
        }

        viewModel.getComics(characterId)
    }

    private fun initDetailAdapter(details: List<DetailParentViewEntity>) {
        binding.recyclerParentDetail.run {
            setHasFixedSize(true)
            adapter = DetailParentAdapter(imageLoader, details)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}