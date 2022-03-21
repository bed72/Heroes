package github.bed72.bedapp.presentation.characters

import org.junit.Test
import org.junit.Rule
import org.junit.Before
import org.mockito.Mock
import org.junit.runner.RunWith
import java.lang.RuntimeException
import androidx.paging.PagingData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertNotNull
import kotlinx.coroutines.test.runTest
import com.nhaarman.mockitokotlin2.any
import org.mockito.junit.MockitoJUnitRunner
import com.nhaarman.mockitokotlin2.whenever
import github.bed72.testing.MainCoroutineRule
import github.bed72.testing.model.CharacterFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import github.bed72.core.usecase.GetCharactersUseCase
import github.bed72.testing.model.CharacterFactory.Hero.ABomb
import github.bed72.testing.model.CharacterFactory.Hero.ThreeDMan

@RunWith(MockitoJUnitRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class CharactersViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Mock
    lateinit var getCharactersUseCase: GetCharactersUseCase

    private lateinit var charactersViewModel: CharactersViewModel

    private val charactersFactory = CharacterFactory()

    private val pagingDataCharacters = PagingData.from(
        listOf(
            charactersFactory.create(ABomb),
            charactersFactory.create(ThreeDMan)
        )
    )

    @Before
    fun setUp() {
        charactersViewModel = CharactersViewModel(getCharactersUseCase)
    }

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




