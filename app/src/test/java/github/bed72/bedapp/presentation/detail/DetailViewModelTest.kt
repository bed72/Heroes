package github.bed72.bedapp.presentation.detail

import org.junit.Test
import org.junit.Rule
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.Assert.assertEquals

import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.isA
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever

import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.ExperimentalCoroutinesApi

import androidx.lifecycle.Observer

import androidx.arch.core.executor.testing.InstantTaskExecutorRule

import github.bed72.bedapp.R
import github.bed72.core.domain.model.Comic
import github.bed72.testing.MainCoroutineRule
import github.bed72.testing.model.ComicFactory
import github.bed72.testing.model.EventFactory
import github.bed72.testing.model.CharacterFactory
import github.bed72.core.usecase.base.ResultStatus
import github.bed72.core.usecase.AddFavoriteUseCase
import github.bed72.core.usecase.CheckFavoriteUseCase
import github.bed72.core.usecase.RemoveFavoriteUseCase
import github.bed72.core.usecase.GetCharacterCategoriesUseCase
import github.bed72.bedapp.presentation.detail.args.DetailViewArg
import github.bed72.bedapp.presentation.detail.redux.LoadLiveData
import github.bed72.testing.model.CharacterFactory.Hero.ThreeDMan
import github.bed72.testing.model.ComicFactory.FakeComic.FakeComicOne
import github.bed72.testing.model.EventFactory.FakeEvent.FakeEventOne
import github.bed72.bedapp.presentation.detail.redux.FavoritesLiveData

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
    private lateinit var checkFavoriteUseCase: CheckFavoriteUseCase

    @Mock
    private lateinit var removeFavoriteUseCase: RemoveFavoriteUseCase

    @Mock
    private lateinit var getCharacterCategoriesUseCase: GetCharacterCategoriesUseCase

    @Mock
    private lateinit var initialStateObserver: Observer<LoadLiveData.States>

    @Mock
    private lateinit var favoriteStateObserver: Observer<FavoritesLiveData.States>

    private lateinit var detailViewModel: DetailViewModel

    private val characterFactory = CharacterFactory().create(ThreeDMan)
    private val comicsFactory = listOf(ComicFactory().create(FakeComicOne))
    private val eventsFactory = listOf(EventFactory().create(FakeEventOne))

    @Before
    fun setUp() {
        detailViewModel = DetailViewModel(
            addFavoriteUseCase,
            mainCoroutineRule.testDispatcherProvider,
            checkFavoriteUseCase,
            removeFavoriteUseCase,
            getCharacterCategoriesUseCase
        ).apply {
            initial.state.observeForever(initialStateObserver)
            favorite.state.observeForever(favoriteStateObserver)
        }
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
            verify(initialStateObserver).onChanged(isA<LoadLiveData.States.Success>())

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
            verify(initialStateObserver).onChanged(isA<LoadLiveData.States.Success>())

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
            verify(initialStateObserver).onChanged(isA<LoadLiveData.States.Success>())

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
            verify(initialStateObserver).onChanged(isA<LoadLiveData.States.Empty>())
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
            verify(initialStateObserver).onChanged(isA<LoadLiveData.States.Error>())
        }

    @Test
    fun `Should notify favorite States with filled favorite icon when check favorite returns true`() =
        runTest {
            // Arrange
            whenever(checkFavoriteUseCase(any()))
                .thenReturn(
                    flowOf(
                        ResultStatus.Success(true)
                    )
                )

            // Action
            detailViewModel.favorite.checkFavorite(characterFactory.id)

            // Assert
            verify(favoriteStateObserver).onChanged(
                isA<FavoritesLiveData.States.Icon>()
            )

            val uiState =
                detailViewModel.favorite.state.value as FavoritesLiveData.States.Icon
            assertEquals(R.drawable.ic_favorite_checked, uiState.icon)
        }

    @Test
    fun `Should notify favorite States with not filled favorite icon when check favorite returns false`() =
        runTest {
            // Arrange
            whenever(checkFavoriteUseCase(any()))
                .thenReturn(
                    flowOf(
                        ResultStatus.Success(false)
                    )
                )

            // Act
            detailViewModel.favorite.checkFavorite(characterFactory.id)

            // Assert
            verify(favoriteStateObserver).onChanged(isA<FavoritesLiveData.States.Icon>())

            val uiState =
                detailViewModel.favorite.state.value as FavoritesLiveData.States.Icon
            assertEquals(R.drawable.ic_favorite_unchecked, uiState.icon)
        }

    @Test
    fun `Should notify favorite States with filled favorite icon when current icon is unchecked`() =
        runTest {
            // Arrange
            whenever(addFavoriteUseCase(any()))
                .thenReturn(
                    flowOf(
                        ResultStatus.Success(Unit)
                    )
                )

            // Act
            detailViewModel.run {
                favorite.currentFavoriteIcon = R.drawable.ic_favorite_unchecked
                favorite.updateFavorite(
                    DetailViewArg(characterFactory.name, characterFactory.imageUrl, characterFactory.id)
                )
            }

            // Assert
            verify(favoriteStateObserver).onChanged(isA<FavoritesLiveData.States.Icon>())

            val uiState =
                detailViewModel.favorite.state.value as FavoritesLiveData.States.Icon
            assertEquals(R.drawable.ic_favorite_checked, uiState.icon)
        }

    @Test
    fun `Should call remove and notify favorite States with filled favorite icon when current icon is checked`() =
        runTest {
            // Arrange
            whenever(removeFavoriteUseCase(any()))
                .thenReturn(
                    flowOf(
                        ResultStatus.Success(Unit)
                    )
                )

            // Act
            detailViewModel.run {
                favorite.currentFavoriteIcon = R.drawable.ic_favorite_checked
                favorite.updateFavorite(
                    DetailViewArg(characterFactory.name, characterFactory.imageUrl, characterFactory.id)
                )
            }

            // Assert
            verify(favoriteStateObserver).onChanged(isA<FavoritesLiveData.States.Icon>())

            val uiState =
                detailViewModel.favorite.state.value as FavoritesLiveData.States.Icon
            assertEquals(R.drawable.ic_favorite_unchecked, uiState.icon)
        }

    companion object {
        private const val COMICS_POSITION = 0
        private const val EVENTS_POSITION = 1
        private const val INITIAL_POSITION = 0
        private const val SIZE_CATEGORIES_ONE = 1
        private const val SIZE_CATEGORIES_TWO = 2
    }
}