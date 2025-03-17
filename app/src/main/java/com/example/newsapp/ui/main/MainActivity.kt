package com.example.newsapp.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.utils.Logger
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityMainBinding.inflate(layoutInflater)
//            loadFragment(HomeFragment())
            clickBottomNav()
            setContentView(binding.root)
        } catch (e: Exception) {
            Logger.logE("MainActivity: Lỗi: ${e.message.toString()}")
        }
    }

    private fun clickBottomNav() {
        binding.apply {
//            bottomNav.setOnItemSelectedListener { item ->
//                when (item.itemId) {
//                    R.id.homeFragment -> {
//                        loadFragment(HomeFragment())
//                    }
////
////                    R.id.summaryFragment -> {
////                        loadFragment(NewsFragment(THOI_SU))
////                    }
//
//                    R.id.savedFragment -> {
//                        loadFragment(SavedFragment())
//                    }
//
//                    R.id.searchFragment -> {
//                        loadFragment(SearchFragment())
//                    }
//
//                    R.id.profileFragment -> {
//                        loadFragment(ProfileFragment())
//                    }
//
//                    else -> loadFragment(HomeFragment())
//                }
//                true
//            }
        }
    }

//    private fun loadFragment(fragmentReplace: Fragment) {
//        supportFragmentManager.beginTransaction().replace(R.id.main_fragment, fragmentReplace)
//            .commit()
//    }
}