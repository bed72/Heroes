package github.bed72.core.usecase

import org.junit.Test
import org.junit.Rule
import org.junit.Before
import org.mockito.Mock
import org.junit.runner.RunWith
import org.junit.Assert.assertTrue
import org.junit.Assert.assertEquals
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import com.nhaarman.mockitokotlin2.whenever
import org.mockito.junit.MockitoJUnitRunner
import github.bed72.testing.MainCoroutineRule
import github.bed72.testing.model.ComicFactory
import github.bed72.testing.model.EventFactory
import github.bed72.core.usecase.base.ResultStatus
import github.bed72.testing.model.CharacterFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import github.bed72.core.data.repository.CharactersRepository
import github.bed72.testing.model.CharacterFactory.Hero.ThreeDMan
import github.bed72.testing.model.ComicFactory.FakeComic.FakeComicOne
import github.bed72.testing.model.EventFactory.FakeEvent.FakeEventOne
import github.bed72.core.usecase.GetCharacterCategoriesUseCase.GetCharacterCategoriesParams

@RunWith(MockitoJUnitRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class GetCharacterCategoriesUseCaseImplTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var charactersRepository: CharactersRepository

    private lateinit var getCharacterCategoriesUseCase: GetCharacterCategoriesUseCase

    private val characterFactory = CharacterFactory().create(ThreeDMan)
    private val comicsFactory = listOf(ComicFactory().create(FakeComicOne))
    private val eventsFactory = listOf(EventFactory().create(FakeEventOne))

    @Before
    fun setup() {
        getCharacterCategoriesUseCase = GetCharacterCategoriesUseCaseImpl(
            charactersRepository = charactersRepository,
            dispatchers = mainCoroutineRule.testDispatcherProvider
        )
    }

    @Test
    fun `Should return Success from ResultStatus when get both requests return success`() = runTest {
        // Arrange
        whenever(charactersRepository.getComics(characterFactory.id)).thenReturn(comicsFactory)
        whenever(charactersRepository.getEvents(characterFactory.id)).thenReturn(eventsFactory)

        // Act
        val result = getCharacterCategoriesUseCase(GetCharacterCategoriesParams(characterFactory.id))

        // Assert
        val resultList = result.toList()
        assertEquals(ResultStatus.Loading, resultList[LOADING_POSITION])
        assertTrue(resultList[SUCCESS_POSITION] is ResultStatus.Success)
    }

    @Test
    fun `Should return Error from ResultStatus when get events request returns error`() = runTest {
        // Arrange
        whenever(charactersRepository.getComics(characterFactory.id)).thenReturn(comicsFactory)
        whenever(charactersRepository.getEvents(characterFactory.id)).thenAnswer {
            throw Throwable()
        }

        // Act
        val result = getCharacterCategoriesUseCase(GetCharacterCategoriesParams(characterFactory.id))

        // Assert
        val resultList = result.toList()
        assertEquals(ResultStatus.Loading, resultList[LOADING_POSITION])
        assertTrue(resultList[ERROR_POSITION] is ResultStatus.Error)
    }

    @Test
    fun `Should return Error from ResultStatus when get comics request returns error`() = runTest {
        // Arrange
        whenever(charactersRepository.getComics(characterFactory.id)).thenAnswer {
            throw Throwable()
        }

        // Act
        val result = getCharacterCategoriesUseCase(GetCharacterCategoriesParams(characterFactory.id))

        // Assert
        val resultList = result.toList()
        assertEquals(ResultStatus.Loading, resultList[LOADING_POSITION])
        assertTrue(resultList[ERROR_POSITION] is ResultStatus.Error)
    }

    companion object {
        private const val LOADING_POSITION = 0
        private const val SUCCESS_POSITION = 1
        private const val ERROR_POSITION = 1
    }
}