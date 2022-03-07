package github.bed72.framework.base

import org.junit.Rule
import github.bed72.testing.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
abstract class BaseTest {
    @Before
    abstract fun setUp()
    @After
    abstract fun tearDown()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()
}