package github.bed72.core.usecase

import org.junit.Test
import org.junit.Rule
import org.junit.Before
import org.mockito.Mock
import org.junit.runner.RunWith
import androidx.paging.PagingConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.mockito.junit.MockitoJUnitRunner
import junit.framework.TestCase.assertNotNull
import github.bed72.testing.MainCoroutineRule
import github.bed72.testing.model.CharacterFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import github.bed72.testing.pagingsource.PagingSourceFactory
import github.bed72.core.data.repository.CharactersRepository

@RunWith(MockitoJUnitRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class GetCharactersUseCaseImplTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var charactersRepository: CharactersRepository

    private lateinit var getCharactersUseCase: GetCharactersUseCase

    private val fakeHero = CharacterFactory().create(CharacterFactory.Hero.ThreeDMan)

    private val fakePagingSource = PagingSourceFactory().create(listOf(fakeHero))

    @Before
    fun setUp() {
        getCharactersUseCase = GetCharactersUseCaseImpl(charactersRepository)
    }

    @Test
    fun `Should validate flow paging data creation when invoke from use case in called`() =
        runTest {
            whenever(charactersRepository.getCharacters(""))
                .thenReturn(fakePagingSource)

            val result = getCharactersUseCase(
                GetCharactersUseCase.GetCharactersParams(
                    "",
                    PagingConfig(20)
                )
            )

            verify(charactersRepository).getCharacters("")

            assertNotNull(result.first())
        }
}