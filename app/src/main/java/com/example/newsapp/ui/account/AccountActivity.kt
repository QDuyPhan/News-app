package com.example.newsapp.ui.account

import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.newsapp.R
import com.example.newsapp.databinding.ActivityAccountBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccountBinding
    private val viewModel by viewModels<AccountViewModel>()
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration


    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolbar = findViewById(R.id.toolbar)
        bottomNavigationView = findViewById(R.id.bottomNav)
        navigationView = findViewById(R.id.navView)
        drawerLayout = findViewById(R.id.account_main)
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        setSupportActionBar(toolbar)
        navigationView.bringToFront()
        navController = findNavController(R.id.nav_host_fragment)
        binding.navView.setupWithNavController(navController)
        appBarConfiguration = AppBarConfiguration(navController.graph, binding.accountMain)
        setupActionBarWithNavController(navController, appBarConfiguration)


        supportActionBar?.hide()
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !viewModel.isReady.value
            }
        }
        // Ẩn BottomNavigationView, NavigationView, Toolbar ở màn hình Login
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.loginFragment || destination.id == R.id.registerFragment) {
                bottomNavigationView.visibility = View.GONE
                navigationView.visibility = View.GONE
                toolbar.visibility = View.GONE
            } else {
                bottomNavigationView.visibility = View.VISIBLE
                navigationView.visibility = View.VISIBLE
                toolbar.visibility = View.VISIBLE
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)

        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}

