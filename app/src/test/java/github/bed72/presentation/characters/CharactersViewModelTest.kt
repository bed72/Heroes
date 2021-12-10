package github.bed72.presentation.characters

import androidx.paging.PagingData
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import github.bed72.bedapp.presentation.characters.CharactersViewModel
import github.bed72.core.usecase.GetCharactersUseCase
import github.bed72.testing.MainCoroutineRule
import github.bed72.testing.model.CharacterFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.lang.RuntimeException

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CharactersViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

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

    @Before
    fun setUp() {
        charactersViewModel = CharactersViewModel(getCharactersUseCase)
    }

    @Test
    fun `🧪 Should validate the paging data object values when calling charactersPagingData`() = runBlockingTest {
        whenever(
            getCharactersUseCase(any())
        ).thenReturn(
            flowOf(
                pagingDataCharacters
            )
        )

        val result = charactersViewModel.charactersPagingData("")

        assertEquals(1, result.count())
    }


    @Test(expected = RuntimeException::class)
    fun `🧪 Should throw an exception when the calling to the use case returns an exception`() = runBlockingTest {
        whenever(getCharactersUseCase(any())).thenThrow(RuntimeException())

        charactersViewModel.charactersPagingData("")
    }
}




