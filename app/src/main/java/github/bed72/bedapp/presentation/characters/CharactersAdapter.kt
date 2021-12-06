package github.bed72.bedapp.presentation.characters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import github.bed72.core.domain.entity.Character

class CharactersAdapter : ListAdapter<Character, CharactersViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CharactersViewHolder.create(parent)

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        holder.bind(getItem(position))
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