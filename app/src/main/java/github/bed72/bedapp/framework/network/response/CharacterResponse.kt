package github.bed72.bedapp.framework.network.response

import github.bed72.core.domain.model.Character

data class CharacterResponse(
    val id: String,
    val name: String,
    val thumbnail: ThumbnailResponse
)

fun CharacterResponse.toCharacterModel() =
    Character(
        name = this.name,
        imageUrl = this.thumbnail.mountPathImage()
    )
