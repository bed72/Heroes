package github.bed72.bedapp.presentation.characters

import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import github.bed72.bedapp.R
import github.bed72.bedapp.data.extensions.asJsonString
import github.bed72.bedapp.framework.di.BaseUrlModule
import github.bed72.bedapp.framework.di.CoroutinesModule
import github.bed72.bedapp.launchFragmentInHiltContainer
import github.bed72.bedapp.presentation.characters.viewholders.CharactersViewHolder
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@UninstallModules(BaseUrlModule::class, CoroutinesModule::class) // Desisntalando o modulo de Prod... O Hilt já vai pegar o Modulo BaseUrl de Test de forma automatica
class CharactersFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private lateinit var server: MockWebServer

    private val navController = TestNavHostController(
        ApplicationProvider.getApplicationContext()
    )

    @Before
    fun setUp() {
        server = MockWebServer().apply {
            start(8080)
        }

        launchFragmentInHiltContainer<CharactersFragment>(
            navHostController = navController
        )
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun shouldShowCharactersWhenViewIsCreated(): Unit = runBlocking {
        server.enqueue(MockResponse().setBody("characters_pag_1.json".asJsonString()))
        delay(500)

        onView(
            withId(R.id.recycler_characters)
        ).check(
            matches(isDisplayed())
        )
    }

    @Test
    fun shouldLoadMoreCharactersWhenNewPageIsRequested(): Unit = runBlocking {
        // Arrange
        with(server) {
            enqueue(MockResponse().setBody("characters_pag_1.json".asJsonString()))
            enqueue(MockResponse().setBody("characters_pag_2.json".asJsonString()))
        }
        delay(500)

        // Action
        onView(
            withId(R.id.recycler_characters)
        ).perform(
            RecyclerViewActions
                .scrollToPosition<CharactersViewHolder>(20)
        )

        // Assert
        onView(
            withText("Amora")
        ).check(
            matches(isDisplayed())
        )
    }

    @Test
    fun shouldShowErrorViewWhenReceivesAnErrorFromApi(): Unit = runBlocking {
        // Arrange
        server.enqueue(MockResponse().setResponseCode(404))
        delay(500)

        onView(
            withId(R.id.text_initial_loading_error)
        ).check(
            matches(isDisplayed())
        )
    }
}