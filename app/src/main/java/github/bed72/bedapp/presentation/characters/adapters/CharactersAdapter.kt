package github.bed72.bedapp.presentation.characters.adapters

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import github.bed72.bedapp.presentation.characters.viewholders.CharactersViewHolder
import github.bed72.core.domain.model.Character

class CharactersAdapter : PagingDataAdapter<Character, CharactersViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CharactersViewHolder.create(parent)

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Character>() {
            override fun areItemsTheSame(
                oldItem: Character,
                newItem: Character
            ) = oldItem.name == newItem.name

            override fun areContentsTheSame(
                oldItem: Character,
                newItem: Character
            ) = oldItem == newItem
        }
    }
}