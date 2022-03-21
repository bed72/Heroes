package github.bed72.bedapp.framework.network.response

import github.bed72.core.domain.model.Event
import com.google.gson.annotations.SerializedName

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