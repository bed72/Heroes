package github.bed72.bedapp.framework.network.response

import com.google.gson.annotations.SerializedName
import github.bed72.core.domain.model.Event

data class EventResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("thumbnail")
    val thumbnail: ThumbnailResponse
)

fun EventResponse.toEventModel() =
    Event(
        id = this.id,
        imageUrl = this.thumbnail.mountPathImage()
    )
