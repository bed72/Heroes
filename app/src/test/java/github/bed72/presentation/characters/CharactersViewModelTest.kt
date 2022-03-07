package github.bed72.presentation.characters

import androidx.paging.PagingData
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import github.bed72.bedapp.presentation.characters.CharactersViewModel
import github.bed72.core.usecase.GetCharactersUseCase
import github.bed72.framework.base.BaseTest
import github.bed72.testing.model.CharacterFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.mockito.Mock
import java.lang.RuntimeException

@OptIn(ExperimentalCoroutinesApi::class)
class CharactersViewModelTest : BaseTest() {

    @Mock
    lateinit var getCharactersUseCase: GetCharactersUseCase

    private lateinit var charactersViewModel: CharactersViewModel

    private val charactersFactory = CharacterFactory()

    private val pagingDataCharacters = PagingData.from(
        listOf(
            charactersFactory.create(CharacterFactory.Hero.ABomb),
            charactersFactory.create(CharacterFactory.Hero.ThreeDMan)
        )
    )

    override fun setUp() {
        charactersViewModel = CharactersViewModel(getCharactersUseCase)
    }

    override fun tearDown() { }

    @Test
    fun `Should validate the paging data object values when calling charactersPagingData`() = runTest {
        whenever(
            getCharactersUseCase(any())
        ).thenReturn(
            flowOf(
                pagingDataCharacters
            )
        )

        val result = charactersViewModel.charactersPagingData("")

        assertNotNull(result.first())
    }


    @Test(expected = RuntimeException::class)
    fun `Should throw an exception when the calling to the use case returns an exception`() = runTest {
        whenever(getCharactersUseCase(any())).thenThrow(RuntimeException())

        charactersViewModel.charactersPagingData("")
    }
}




