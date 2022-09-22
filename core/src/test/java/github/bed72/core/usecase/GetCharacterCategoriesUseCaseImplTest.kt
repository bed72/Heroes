package github.bed72.core.usecase

import com.nhaarman.mockitokotlin2.whenever
import github.bed72.core.data.repository.characters.CharacterRepository
import github.bed72.core.usecase.GetCharacterCategoriesUseCase.GetCharacterCategoriesParams
import github.bed72.core.usecase.base.ResultStatus
import github.bed72.testing.MainCoroutineRule
import github.bed72.testing.model.CharacterFactory
import github.bed72.testing.model.CharacterFactory.Hero.ThreeDMan
import github.bed72.testing.model.ComicFactory
import github.bed72.testing.model.ComicFactory.FakeComic.FakeComicOne
import github.bed72.testing.model.EventFactory
import github.bed72.testing.model.EventFactory.FakeEvent.FakeEventOne
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class GetCharacterCategoriesUseCaseImplTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var characterRepository: CharacterRepository

    private lateinit var getCharacterCategoriesUseCase: GetCharacterCategoriesUseCase

    private val characterFactory = CharacterFactory().create(ThreeDMan)
    private val comicsFactory = listOf(ComicFactory().create(FakeComicOne))
    private val eventsFactory = listOf(EventFactory().create(FakeEventOne))

    @Before
    fun setup() {
        getCharacterCategoriesUseCase = GetCharacterCategoriesUseCaseImpl(
            characterRepository = characterRepository,
            dispatchers = mainCoroutineRule.testDispatcherProvider
        )
    }

    @Test
    fun `Should return Success from ResultStatus when get both requests return success`() = runTest {
        // Arrange
        whenever(characterRepository.getComics(characterFactory.id)).thenReturn(comicsFactory)
        whenever(characterRepository.getEvents(characterFactory.id)).thenReturn(eventsFactory)

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
        whenever(characterRepository.getComics(characterFactory.id)).thenReturn(comicsFactory)
        whenever(characterRepository.getEvents(characterFactory.id)).thenAnswer {
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
        whenever(characterRepository.getComics(characterFactory.id)).thenAnswer {
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
