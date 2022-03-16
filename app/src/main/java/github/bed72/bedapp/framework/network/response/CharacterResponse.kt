package github.bed72.bedapp.framework.network.response

import com.google.gson.annotations.SerializedName
import github.bed72.core.domain.model.Character

data class CharacterResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("thumbnail")
    val thumbnail: ThumbnailResponse
)

fun CharacterResponse.toCharacterModel() =
    Character(
        id = this.id,
        name = this.name,
        imageUrl = this.thumbnail.mountPathImage()
    )
