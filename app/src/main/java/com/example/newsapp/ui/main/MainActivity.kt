package com.example.newsapp.ui.main

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.newsapp.R
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.ui.home.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        loadFragment(HomeFragment())
        binding.apply {
            val navController = findNavController(R.id.main_fragment)
            bottomNav.setupWithNavController(navController)
        }
    }

    private fun loadFragment(fragmentReplace: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_fragment, fragmentReplace)
            .commit()
    }
}