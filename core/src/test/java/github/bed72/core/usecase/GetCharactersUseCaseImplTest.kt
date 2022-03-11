package github.bed72.core.usecase

import androidx.paging.PagingConfig
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import github.bed72.core.data.repository.CharactersRepository
import github.bed72.testing.base.BaseTest
import github.bed72.testing.model.CharacterFactory
import github.bed72.testing.pagingsource.PagingSourceFactory
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mock

@OptIn(ExperimentalCoroutinesApi::class)
class GetCharactersUseCaseImplTest : BaseTest() {

    @Mock
    private lateinit var charactersRepository: CharactersRepository

    private lateinit var getCharactersUseCase: GetCharactersUseCase

    private val fakeHero = CharacterFactory().create(CharacterFactory.Hero.ThreeDMan)

    private val fakePagingSource = PagingSourceFactory().create(listOf(fakeHero))

    override fun setUp() {
        getCharactersUseCase = GetCharactersUseCaseImpl(charactersRepository)
    }

    override fun tearDown() { }

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