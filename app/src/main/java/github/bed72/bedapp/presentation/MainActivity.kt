package github.bed72.bedapp.presentation

import android.os.Bundle
import android.view.View

import dagger.hilt.android.AndroidEntryPoint

import github.bed72.bedapp.R
import github.bed72.bedapp.databinding.ActivityMainBinding

import androidx.navigation.NavController
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setNavigationController()
        setAppBar()
        setNavigationBar()
    }

    private fun setNavigationController() {
        val navigationContainerFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_container) as NavHostFragment

        navController = navigationContainerFragment.navController
        binding.bottomNavMain.setupWithNavController(navController)
    }

    private fun setNavigationBar() {
        navController.addOnDestinationChangedListener {_, destination, _ ->
            visibilityNavBar(destination.id)
            visibilityToolBar(destination.id)
            visibilityGoBackInToolBar(destination.id)
        }
    }

    private fun setAppBar() {
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.characters_fragment,
                R.id.favorites_fragment,
                R.id.about_fragment
            )
        )
        binding.toolbarApp.setupWithNavController(navController, appBarConfiguration)
    }

    private fun visibilityGoBackInToolBar(destination: Int) {
        val isTopLevelDestination = appBarConfiguration.topLevelDestinations.contains(destination)

        if (!isTopLevelDestination) binding.toolbarApp.setNavigationIcon(R.drawable.ic_back)
    }

    private fun visibilityNavBar(destination: Int) {
        val visibility = when (destination) {
            R.id.about_fragment -> VISIBLE
            R.id.favorites_fragment -> VISIBLE
            R.id.characters_fragment -> VISIBLE
            else -> GONE
        }

        binding.bottomNavMain.visibility = visibility
    }

    private fun visibilityToolBar(destination: Int) {
        val visibility = when (destination) {
            R.id.about_fragment -> VISIBLE
            R.id.detail_fragment -> VISIBLE
            R.id.favorites_fragment -> VISIBLE
            R.id.characters_fragment -> VISIBLE
            else -> GONE
        }

        binding.toolbarApp.visibility = visibility
    }

    companion object {
        private const val GONE = View.GONE
        private const val VISIBLE = View.VISIBLE
    }
}
