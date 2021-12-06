package github.bed72.bedapp.framework.network.response

data class DataContainerResponse(
    val offset: Int,
    val total: Int,
    val results: List<CharacterResponse>
)
