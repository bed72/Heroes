package github.bed72.bedapp.presentation.characters.adapters

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

import github.bed72.bedapp.presentation.characters.viewholders.CharactersRefreshStateViewHolder

class CharactersRefreshStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<CharactersRefreshStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ) = CharactersRefreshStateViewHolder.create(parent, retry)

    override fun onBindViewHolder(holder: CharactersRefreshStateViewHolder, loadState: LoadState) =
        holder.bind(loadState)
}