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
    private val listCategory: List<CategoryResponse>? = null,
    private val onDeleteSelected: (() -> Unit)? = null,
    private val previousScreen: String
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    private val fragments = mutableListOf<NewsFragment>()
    override fun createFragment(position: Int): Fragment {
        val categoryName = listCategory?.get(position)?.name ?: ""
        val fragment = NewsFragment.newInstance(categoryName, previousScreen)
        fragments.add(fragment)
        return fragment
    }

    override fun getItemCount(): Int {
        return listCategory!!.size
    }

    fun getFragments(): List<NewsFragment> = fragments
}