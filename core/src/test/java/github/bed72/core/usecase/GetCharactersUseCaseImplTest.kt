package github.bed72.core.usecase

import org.junit.Test
import org.junit.Rule
import org.junit.Before
import org.junit.runner.RunWith

import androidx.paging.PagingData
import androidx.paging.PagingConfig

import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever

import junit.framework.TestCase.assertNotNull

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.ExperimentalCoroutinesApi

import github.bed72.testing.MainCoroutineRule
import github.bed72.testing.model.CharacterFactory
import github.bed72.core.data.repository.storage.StorageRepository
import github.bed72.core.data.repository.characters.CharacterRepository

@RunWith(MockitoJUnitRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class GetCharactersUseCaseImplTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var storageRepository: StorageRepository

    @Mock
    private lateinit var characterRepository: CharacterRepository

    private lateinit var getCharactersUseCase: GetCharactersUseCase

    private val fakeHero = CharacterFactory().create(CharacterFactory.Hero.ThreeDMan)

    private val fakePagingData = PagingData.from(listOf(fakeHero))

    @Before
    fun setUp() {
        getCharactersUseCase = GetCharactersUseCaseImpl(storageRepository, characterRepository)
    }

    @Test
    fun `Should validate flow paging data creation when invoke from use case in called`() =
        runTest {
            val query = "spider"
            val orderBy = "ascending"
            val pagingConfig = PagingConfig(20)

            whenever(characterRepository.getCharacters(query, orderBy, pagingConfig))
                .thenReturn(flowOf(fakePagingData))

            whenever(storageRepository.sorting).thenReturn(flowOf(orderBy))

            val result = getCharactersUseCase(
                GetCharactersUseCase.GetCharactersParams(query, pagingConfig)
            )

            verify(characterRepository).getCharacters(query, orderBy, pagingConfig)

            assertNotNull(result.first())
        }
}