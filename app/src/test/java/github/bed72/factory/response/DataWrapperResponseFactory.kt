package github.bed72.factory.response

import github.bed72.bedapp.framework.network.response.CharacterResponse
import github.bed72.bedapp.framework.network.response.DataWrapperResponse
import github.bed72.bedapp.framework.network.response.DataContainerResponse
import github.bed72.bedapp.framework.network.response.ThumbnailResponse
import github.bed72.core.domain.model.Character
import github.bed72.core.domain.model.CharacterPaging


class DataWrapperResponseFactory {

    fun create() = CharacterPaging(
        offset = 0,
        total = 2,
        character = listOf(
            Character(
                id = 1011334,
                name = "3-D Man",
                imageUrl = "https://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784.jpg"
            ),
            Character(
                id = 1017100,
                name = "A-Bomb (HAS)",
                imageUrl = "https://i.annihil.us/u/prod/marvel/i/mg/3/20/5232158de5b16.jpg"
            )
        )
    )
}