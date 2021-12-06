package github.bed72.bedapp.presentation.characters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import github.bed72.bedapp.R
import github.bed72.bedapp.databinding.ItemCharacterBinding
import github.bed72.core.domain.model.Character

class CharactersViewHolder(
    itemCharacterBinding: ItemCharacterBinding
) : RecyclerView.ViewHolder(itemCharacterBinding.root) {

    private val imageCharacter = itemCharacterBinding.imageCharacter
    private val textNameCharacter = itemCharacterBinding.textNameCharacter

    fun bind(character: Character) {
        textNameCharacter.text = character.name
        Glide.with(itemView)
            .load(character.imageUrl)
            .error(R.drawable.ic_img_loading_error)
            .fallback(R.drawable.ic_img_loading_error)
            .into(imageCharacter)
    }

    companion object {
        fun create(parent: ViewGroup): CharactersViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val itemBinding = ItemCharacterBinding.inflate(inflater, parent, false)

            return CharactersViewHolder(itemBinding)
        }
    }
}