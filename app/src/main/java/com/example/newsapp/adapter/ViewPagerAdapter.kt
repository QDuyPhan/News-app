package com.example.newsapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.newsapp.data.response.CategoryResponse
import com.example.newsapp.ui.news.NewsFragment

class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val listDescription: List<String>,
    private val listCategory: List<CategoryResponse>
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment {
        return NewsFragment(listCategory[position].name)
    }

    override fun getItemCount(): Int {
        return listDescription.size
    }
}