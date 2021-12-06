package github.bed72.bedapp.framework.network.response


data class ThumbnailResponse(
    val path: String,
    val extension: String
)

fun ThumbnailResponse.mountPathImage() =
    "${this.path}.${this.extension}".replace("http", "https")