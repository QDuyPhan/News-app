package com.example.newsapp.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.newsapp.R
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.ui.category.CategoryFragment
import com.example.newsapp.ui.home.HomeFragment
import com.example.newsapp.ui.news.NewsFragment
import com.example.newsapp.ui.profile.ProfileFragment
import com.example.newsapp.ui.saved.SavedFragment
import com.example.newsapp.ui.search.SearchFragment
import com.example.newsapp.ui.summary.SummaryFragment
import com.example.newsapp.utils.Constants.THOI_SU
import com.example.newsapp.utils.Logger
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityMainBinding.inflate(layoutInflater)
            loadFragment(HomeFragment())
            clickBottomNav()
            setContentView(binding.root)
        } catch (e: Exception) {
            Logger.logE("MainActivity: Lỗi: ${e.message.toString()}")
        }
    }

    private fun clickBottomNav() {
        binding.apply {
            bottomNav.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.homeFragment -> {
                        loadFragment(HomeFragment())
                    }

                    R.id.summaryFragment -> {
                        loadFragment(NewsFragment(THOI_SU))
                    }

                    R.id.savedFragment -> {
                        loadFragment(SavedFragment())
                    }

                    R.id.searchFragment -> {
                        loadFragment(SearchFragment())
                    }

                    R.id.profileFragment -> {
                        loadFragment(ProfileFragment())
                    }

                    else -> loadFragment(HomeFragment())
                }
                true
            }
        }
    }

    private fun loadFragment(fragmentReplace: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.main_fragment, fragmentReplace)
            .commit()
    }
}