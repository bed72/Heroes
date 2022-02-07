package github.bed72.bedapp.presentation.characters.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import github.bed72.bedapp.R
import github.bed72.bedapp.databinding.ItemCharacterBinding
import github.bed72.bedapp.utils.OnCharacterItemClick
import github.bed72.core.domain.model.Character

class CharactersViewHolder(
    itemCharacterBinding: ItemCharacterBinding,
    private val onItemClick: OnCharacterItemClick
) : RecyclerView.ViewHolder(itemCharacterBinding.root) {

    private val imageCharacter = itemCharacterBinding.imageCharacter
    private val textNameCharacter = itemCharacterBinding.textNameCharacter

    fun bind(character: Character) {
        textNameCharacter.text = character.name
        imageCharacter.transitionName = character.name
        Glide.with(itemView)
            .load(character.imageUrl)
            .error(R.drawable.ic_img_loading_error)
            .fallback(R.drawable.ic_img_loading_error)
            .into(imageCharacter)

        // Passando o 'imageCharacter' para que o graph conheça-o e consiga fazer a animação
        itemView.setOnClickListener {
            onItemClick.invoke(character, imageCharacter)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onItemClick: OnCharacterItemClick
        ): CharactersViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val itemBinding = ItemCharacterBinding.inflate(inflater, parent, false)

            return CharactersViewHolder(itemBinding, onItemClick)
        }
    }
}