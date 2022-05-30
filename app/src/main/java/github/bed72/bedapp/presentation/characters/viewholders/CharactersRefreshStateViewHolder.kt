package github.bed72.bedapp.presentation.characters.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import github.bed72.bedapp.databinding.ItemCharacterLoadMoreStateBinding
import github.bed72.bedapp.databinding.ItemCharacterRefreshStateBinding

class CharactersRefreshStateViewHolder(
    itemBinding: ItemCharacterRefreshStateBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(itemBinding.root) {

    private val binding = ItemCharacterRefreshStateBinding.bind(itemView)
    private val progressBarLoadMore = binding.progressRefresh
    private val textTryAgainMessage = binding.textTryAgain.also {
        it.setOnClickListener {
            retry()
        }
    }

    fun bind(loadState: LoadState) {
        progressBarLoadMore.isVisible = loadState is LoadState.Loading
        textTryAgainMessage.isVisible = loadState is LoadState.Error
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): CharactersRefreshStateViewHolder {
            val itemBinding = ItemCharacterRefreshStateBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)

            return CharactersRefreshStateViewHolder(itemBinding, retry)
        }
    }
}