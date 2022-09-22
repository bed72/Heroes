package github.bed72.bedapp.framework.network.response

import com.google.gson.annotations.SerializedName
import github.bed72.core.domain.model.Comic

data class ComicResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("thumbnail")
    val thumbnail: ThumbnailResponse
)

fun ComicResponse.toComicModel() =
    Comic(
        id = this.id,
        imageUrl = this.thumbnail.mountPathImage()
    )
