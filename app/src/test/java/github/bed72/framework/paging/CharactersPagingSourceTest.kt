package github.bed72.framework.paging

import androidx.paging.PagingSource
import com.nhaarman.mockitokotlin2.whenever
import github.bed72.bedapp.framework.paging.CharactersPagingSource
import github.bed72.core.data.repository.CharactersRemoteDataSource
import github.bed72.factory.response.DataWrapperResponseFactory
import github.bed72.testing.model.CharacterFactory
import org.junit.Assert.assertEquals
import org.junit.Test
import com.nhaarman.mockitokotlin2.any
import github.bed72.core.domain.model.Character
import github.bed72.framework.base.BaseTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.mockito.Mock
import java.lang.RuntimeException

@OptIn(ExperimentalCoroutinesApi::class)
class CharactersPagingSourceTest : BaseTest() {

    @Mock
    private lateinit var charactersRemoteDataSource: CharactersRemoteDataSource

    private lateinit var charactersPagingSource: CharactersPagingSource

    private val fakeDataWrapperResponse = DataWrapperResponseFactory()

    private val fakeCharacters = CharacterFactory()

    override fun setUp() {
        charactersPagingSource = CharactersPagingSource("", charactersRemoteDataSource)
    }

    override fun tearDown() { }

    @Test
    fun `Should return a success load result when method load is called`() = runTest {
        // Arrange
        whenever(charactersRemoteDataSource.fetchCharacters(any()))
            .thenReturn(fakeDataWrapperResponse.create())

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
            fakeCharacters.create(CharacterFactory.Hero.ThreeDMan),
            fakeCharacters.create(CharacterFactory.Hero.ABomb)
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
        whenever(charactersRemoteDataSource.fetchCharacters(any()))
            .thenThrow(exception)

        // Act
        val result = charactersPagingSource.load(
            PagingSource.LoadParams.Refresh(
                null,
                loadSize = 2,
                false
            )
        )

        //Assert
        assertEquals(
            PagingSource.LoadResult.Error<Int, Character>(exception),
            result
        )
    }
}