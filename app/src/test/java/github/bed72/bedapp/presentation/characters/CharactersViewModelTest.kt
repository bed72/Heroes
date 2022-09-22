package github.bed72.bedapp.presentation.characters

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Test
import org.junit.Rule
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.Assert.assertNotNull

import androidx.paging.PagingData

import java.lang.RuntimeException

import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever

import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.ExperimentalCoroutinesApi

import github.bed72.testing.MainCoroutineRule
import github.bed72.testing.model.CharacterFactory
import github.bed72.core.usecase.GetCharactersUseCase
import github.bed72.testing.model.CharacterFactory.Hero.ABomb
import github.bed72.testing.model.CharacterFactory.Hero.ThreeDMan
import java.util.*

@RunWith(MockitoJUnitRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class CharactersViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

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
        charactersViewModel = CharactersViewModel(
            mainCoroutineRule.testDispatcherProvider,
            getCharactersUseCase
        )
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

        charactersViewModel.search()
        val response = charactersViewModel.state.value as CharactersViewModel.States.SearchResult

        assertNotNull(response.data)
    }


    @Test(expected = RuntimeException::class)
    fun `Should throw an exception when the calling to the use case returns an exception`() = runTest {
        whenever(getCharactersUseCase(any())).thenThrow(RuntimeException())

        charactersViewModel.search()
    }
}




