package github.bed72.bedapp.framework.base

import org.junit.Rule
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import github.bed72.testing.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import androidx.arch.core.executor.testing.InstantTaskExecutorRule

@RunWith(MockitoJUnitRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
abstract class BaseTest {
    @Before
    abstract fun setUp()
    @After
    abstract fun tearDown()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule() // para vincular LiveData
}