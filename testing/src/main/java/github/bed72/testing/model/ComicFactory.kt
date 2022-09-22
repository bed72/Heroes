package github.bed72.testing.model

import github.bed72.core.domain.model.Comic
import github.bed72.testing.model.ComicFactory.FakeComic.FakeComicOne

class ComicFactory {
    fun create(comic: FakeComic) = when (comic) {
        FakeComicOne -> Comic(
            2211506,
            "https://fakecomicurl.jpg"
        )
    }

    sealed class FakeComic {
        object FakeComicOne : FakeComic()
    }
}
