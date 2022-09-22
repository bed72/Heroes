package github.bed72.bedapp.framework.paging

import androidx.paging.PagingSource
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import github.bed72.bedapp.factory.response.CharacterPagingFactory
import github.bed72.core.data.repository.characters.CharacterRemoteDataSource
import github.bed72.core.domain.model.Character
import github.bed72.testing.MainCoroutineRule
import github.bed72.testing.model.CharacterFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.lang.RuntimeException

@RunWith(MockitoJUnitRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class CharactersPagingSourceTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var characterRemoteDataSource: CharacterRemoteDataSource

    private lateinit var charactersPagingSource: CharactersPagingSource

    private val fakeDataWrapperResponseFactory = CharacterPagingFactory()

    private val charactersFactory = CharacterFactory()

    @Before
    fun setUp() {
        charactersPagingSource = CharactersPagingSource("", characterRemoteDataSource)
    }

    @Test
    fun `Should return a success load result when method load is called`() = runTest {
        // Arrange
        whenever(characterRemoteDataSource.fetchCharacters(any()))
            .thenReturn(fakeDataWrapperResponseFactory.create())

        // Act
        val result = charactersPagingSource.load(
            // Refresh() metodo padrão chamado pela primeira vez
            PagingSource.LoadParams.Refresh(
                null, // A implementação que fiz trata esse null
                loadSize = 2,
                false
            )
        )

        // Assert
        val expected = listOf(
            charactersFactory.create(CharacterFactory.Hero.ThreeDMan),
            charactersFactory.create(CharacterFactory.Hero.ABomb)
        )

        assertEquals(
            PagingSource.LoadResult.Page(
                data = expected,
                prevKey = null,
                nextKey = 20
            ),
            result
        )
    }

    @Test
    fun `Should return a error load result when load is called`() = runTest {
        // Arrange
        val exception = RuntimeException()
        whenever(characterRemoteDataSource.fetchCharacters(any()))
            .thenThrow(exception)

        // Act
        val result = charactersPagingSource.load(
            PagingSource.LoadParams.Refresh(
                null,
                loadSize = 2,
                false
            )
        )

        // Assert
        assertEquals(
            PagingSource.LoadResult.Error<Int, Character>(exception),
            result
        )
    }
}
