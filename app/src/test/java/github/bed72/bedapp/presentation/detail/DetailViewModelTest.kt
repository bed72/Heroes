package github.bed72.bedapp.presentation.detail

import org.junit.Test
import org.junit.Rule
import org.junit.Before
import org.mockito.Mock
import github.bed72.bedapp.R
import org.junit.runner.RunWith
import androidx.lifecycle.Observer
import org.junit.Assert.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.isA
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.mockito.junit.MockitoJUnitRunner
import github.bed72.core.domain.model.Comic
import github.bed72.testing.MainCoroutineRule
import github.bed72.testing.model.ComicFactory
import github.bed72.testing.model.EventFactory
import github.bed72.testing.model.CharacterFactory
import github.bed72.core.usecase.base.ResultStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import github.bed72.core.usecase.AddFavoriteUseCase
import github.bed72.core.usecase.GetCharacterCategoriesUseCase
import github.bed72.testing.model.CharacterFactory.Hero.ThreeDMan
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import github.bed72.testing.model.ComicFactory.FakeComic.FakeComicOne
import github.bed72.testing.model.EventFactory.FakeEvent.FakeEventOne
import github.bed72.bedapp.presentation.detail.redux.LoadLiveData

@RunWith(MockitoJUnitRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule() // para vincular LiveData

    @Mock
    private lateinit var addFavoriteUseCase: AddFavoriteUseCase

    @Mock
    private lateinit var getCharacterCategoriesUseCase: GetCharacterCategoriesUseCase

    @Mock
    private lateinit var uiStateObserver: Observer<LoadLiveData.States>

    private lateinit var detailViewModel: DetailViewModel

    private val characterFactory = CharacterFactory().create(ThreeDMan)
    private val comicsFactory = listOf(ComicFactory().create(FakeComicOne))
    private val eventsFactory = listOf(EventFactory().create(FakeEventOne))

    @Before
    fun setUp() {
        detailViewModel = DetailViewModel(
            addFavoriteUseCase,
            mainCoroutineRule.testDispatcherProvider,
            getCharacterCategoriesUseCase
        ).apply { initial.state.observeForever(uiStateObserver) }
    }

    @Test
    fun `Should notify uiState with Success from UiState when get character categories returns success`() =
        runTest {
            // Arrange
            whenever(getCharacterCategoriesUseCase(any())).thenReturn(
                flowOf(
                    ResultStatus.Success(
                        comicsFactory to eventsFactory
                    )
                )
            )

            // Action
            detailViewModel.initial.load(characterFactory.id)

            // Assert
            // isA<>() Se for do tipo (verifica tipo)
            verify(uiStateObserver).onChanged(isA<LoadLiveData.States.Success>())

            // Verificar valores
            val uiStateSuccess = detailViewModel.initial.state.value
                    as LoadLiveData.States.Success
            val categoriesParentList = uiStateSuccess.detailParentList

            assertEquals(SIZE_CATEGORIES_TWO, categoriesParentList.size)
            assertEquals(
                R.string.details_comics_category, categoriesParentList[COMICS_POSITION].categoryStringResId
            )
            assertEquals(
                R.string.details_events_category, categoriesParentList[EVENTS_POSITION].categoryStringResId
            )
        }

    @Test
    fun `Should notify uiState with Success from UiState when get character categories returns only comics`() =
        runTest {
            // Arrange
            whenever(getCharacterCategoriesUseCase(any())).thenReturn(
                flowOf(
                    ResultStatus.Success(
                        comicsFactory to emptyList()
                    )
                )
            )

            // Action
            detailViewModel.initial.load(characterFactory.id)

            // Assert
            verify(uiStateObserver).onChanged(isA<LoadLiveData.States.Success>())

            val uiStateSuccess = detailViewModel.initial.state.value
                    as LoadLiveData.States.Success
            val categoriesParentList = uiStateSuccess.detailParentList

            assertEquals(SIZE_CATEGORIES_ONE, categoriesParentList.size)
            assertEquals(
                R.string.details_comics_category, categoriesParentList[COMICS_POSITION].categoryStringResId
            )
        }




    @Test
    fun `Should notify uiState with Success from UiState when get character categories returns only events`() =
        runTest {
            // Arrange
            whenever(getCharacterCategoriesUseCase(any())).thenReturn(
                flowOf(
                    ResultStatus.Success(
                        emptyList<Comic>() to eventsFactory
                    )
                )
            )

            // Action
            detailViewModel.initial.load(characterFactory.id)

            // Assert
            verify(uiStateObserver).onChanged(isA<LoadLiveData.States.Success>())

            val uiStateSuccess = detailViewModel.initial.state.value
                as LoadLiveData.States.Success
            val categoriesParentList = uiStateSuccess.detailParentList

            assertEquals(SIZE_CATEGORIES_ONE, categoriesParentList.size)
            assertEquals(
                R.string.details_events_category, categoriesParentList[INITIAL_POSITION].categoryStringResId
            )
        }

    @Test
    fun `Should notify uiState with Empty from UiState when get character categories returns an empty result list`() =
        runTest {
            // Arrange
            whenever(getCharacterCategoriesUseCase(any())).thenReturn(
                flowOf(
                    ResultStatus.Success(
                        emptyList<Comic>() to emptyList()
                    )
                )
            )

            // Action
            detailViewModel.initial.load(characterFactory.id)

            // Assert
            verify(uiStateObserver).onChanged(isA<LoadLiveData.States.Empty>())
        }

    @Test
    fun `Should notify uiState with Error from UiState when get character categories returns an exception`() =
        runTest {
            // Arrange
            whenever(getCharacterCategoriesUseCase(any())).thenReturn(
                flowOf(
                    ResultStatus.Error(Throwable())
                )
            )

            // Action
            detailViewModel.initial.load(characterFactory.id)

            // Assert
            verify(uiStateObserver).onChanged(isA<LoadLiveData.States.Error>())
        }

    companion object {
        private const val COMICS_POSITION = 0
        private const val EVENTS_POSITION = 1
        private const val INITIAL_POSITION = 0
        private const val SIZE_CATEGORIES_ZERO = 0
        private const val SIZE_CATEGORIES_ONE = 1
        private const val SIZE_CATEGORIES_TWO = 2
    }
}