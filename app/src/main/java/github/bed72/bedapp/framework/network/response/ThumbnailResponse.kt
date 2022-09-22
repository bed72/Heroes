package github.bed72.bedapp.framework.network.response

import com.google.gson.annotations.SerializedName

data class ThumbnailResponse(
    @SerializedName("path")
    val path: String,
    @SerializedName("extension")
    val extension: String
)

fun ThumbnailResponse.mountPathImage() =
    "${this.path}.${this.extension}".replace("http", "https")
