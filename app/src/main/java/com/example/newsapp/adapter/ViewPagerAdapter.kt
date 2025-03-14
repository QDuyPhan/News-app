package com.example.newsapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.newsapp.data.remote.response.CategoryResponse
import com.example.newsapp.ui.news.NewsFragment

class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val listCategory: List<CategoryResponse>? = null
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment {
        val categoryName = listCategory?.get(position)?.name ?: ""
        return NewsFragment.newInstance(categoryName)
    }

    override fun getItemCount(): Int {
        return listCategory!!.size
    }
}