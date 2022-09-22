package github.bed72.bedapp.presentation.characters.adapters

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import github.bed72.bedapp.presentation.characters.viewholders.CharactersLoadMoreStateViewHolder

class CharactersLoadMoreStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<CharactersLoadMoreStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ) = CharactersLoadMoreStateViewHolder.create(parent, retry)

    override fun onBindViewHolder(holder: CharactersLoadMoreStateViewHolder, loadState: LoadState) =
        holder.bind(loadState)
}
