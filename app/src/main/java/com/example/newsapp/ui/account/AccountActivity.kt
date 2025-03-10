package com.example.newsapp.ui.account

import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.newsapp.R
import com.example.newsapp.databinding.ActivityAccountBinding
import com.example.newsapp.utils.Constants.UNACTIVATE
import com.example.newsapp.utils.Logger
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccountBinding
    private val viewModel by viewModels<AccountViewModel>()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !viewModel.isReady.value
            }
        }
        navController = findNavController(R.id.nav_host_fragment)
        binding.bottomNav.setupWithNavController(navController)
        Logger.logI("AccountActivity isLogin: ${viewModel.isLogin()}")
        if (viewModel.isLogin() == UNACTIVATE) {
            navController.navigate(R.id.loginFragment)
        } else {
            navController.navigate(R.id.homeFragment)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.loginFragment || destination.id == R.id.registerFragment) {
                binding.bottomNav.visibility = View.GONE
            } else {
                binding.bottomNav.visibility = View.VISIBLE
            }
        }
    }
}

