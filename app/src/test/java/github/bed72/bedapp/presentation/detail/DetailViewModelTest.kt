package github.bed72.bedapp.presentation.detail

import org.mockito.Mock
import androidx.lifecycle.Observer
import github.bed72.testing.model.ComicFactory
import github.bed72.testing.model.EventFactory
import github.bed72.bedapp.framework.base.BaseTest
import github.bed72.testing.model.CharacterFactory
import github.bed72.core.usecase.GetCharacterCategoriesUseCase
import github.bed72.testing.model.CharacterFactory.Hero.ThreeDMan
import github.bed72.testing.model.ComicFactory.FakeComic.FakeComicOne
import github.bed72.testing.model.EventFactory.FakeEvent.FakeEventOne
import github.bed72.bedapp.presentation.detail.DetailViewModel.UiState

class DetailViewModelTest : BaseTest() {

    @Mock
    private lateinit var getCharacterCategoriesUseCase: GetCharacterCategoriesUseCase

    @Mock
    private lateinit var uiStateObserver: Observer<UiState>

    private lateinit var detailViewModel: DetailViewModel

    private val characterFactory = CharacterFactory().create(ThreeDMan)
    private val comicsFactory = listOf(ComicFactory().create(FakeComicOne))
    private val eventsFactory = listOf(EventFactory().create(FakeEventOne))

    override fun setUp() {
        detailViewModel = DetailViewModel(getCharacterCategoriesUseCase)
        // Vinculação sempre depois do UseCase, toda vez que for notificado no viewModel vou receber aqui...
        detailViewModel.uiState.observeForever(uiStateObserver)
    }

    override fun tearDown() { }
}