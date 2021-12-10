package github.bed72.core.usecase

import androidx.paging.PagingConfig
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import github.bed72.core.data.repository.CharactersRepository
import github.bed72.testing.MainCoroutineRule
import github.bed72.testing.model.CharacterFactory
import github.bed72.testing.pagingsource.PagingSourceFactory
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GetCharactersUseCaseImplTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

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
        runBlockingTest {
            whenever(charactersRepository.getCharacters(""))
                .thenReturn(fakePagingSource)

            val result = getCharactersUseCase(
                GetCharactersUseCase.GetCharactersParams(
                    "",
                    PagingConfig(20)
                )
            )

            //  times(0)
            verify(charactersRepository).getCharacters("")

            assertNotNull(result.first())
        }
}