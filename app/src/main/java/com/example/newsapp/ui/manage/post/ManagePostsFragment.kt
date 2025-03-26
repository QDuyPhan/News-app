package com.example.newsapp.ui.manage.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.newsapp.R
import com.example.newsapp.adapter.ViewPagerAdapter
import com.example.newsapp.data.remote.response.CategoryResponse
import com.example.newsapp.databinding.FragmentManagePostsBinding
import com.example.newsapp.ui.base.BaseFragment
import com.example.newsapp.ui.home.HomeViewModel
import com.example.newsapp.utils.Logger
import com.example.newsapp.utils.setOnSingClickListener
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManagePostsFragment : BaseFragment<FragmentManagePostsBinding>() {
    private val managePostsViewModel by viewModels<ManagePostsViewModel>()
    private val homeViewModel by viewModels<HomeViewModel>()
    private var listCategory: List<CategoryResponse>? = null
    private var viewPagerAdapter: ViewPagerAdapter? = null

    override fun inflateBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentManagePostsBinding {
        return FragmentManagePostsBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupData()
        handleBackClick()
        showAlertPostNews()
    }

    private fun setupUI() {
        binding.apply {
            searchView.isEnabled = false
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean = false
                override fun onQueryTextChange(newText: String?): Boolean {
                    performSearch(newText)
                    return true
                }
            })
        }
    }

    private fun setupData() {
        observeResource(liveData = homeViewModel.listCategory, onSuccess = { response ->
            listCategory = response.result
            initViewPager()
            binding.searchView.isEnabled = true
        }, onError = {
            Logger.logE(it)
        }, onLoading = {
            Logger.logI("Loading categories...")
        })
    }

    private fun initViewPager() {
        val fragmentManager = childFragmentManager
        viewPagerAdapter = ViewPagerAdapter(
            fragmentManager, lifecycle, listCategory, previousScreen = "manage"
        )
        binding.viewPager2.adapter = viewPagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            tab.text = listCategory?.getOrNull(position)?.description
        }.attach()

        binding.actionBar.setOnClickRight {
            viewPagerAdapter?.getFragments()?.forEach { fragment ->
                fragment.toggleDeleteModeOrPerformDelete()
            }
        }
    }

    private fun performSearch(keyword: String?) {
        val adapter = viewPagerAdapter ?: return
        val currentFragment = adapter.getFragments().getOrNull(binding.viewPager2.currentItem)
        currentFragment?.searchNews(keyword.orEmpty())
    }

    private fun handleBackClick() {
        binding.actionBar.setOnClickLeft {
            findNavController().popBackStack()
        }
    }

    private fun showAlertPostNews() {
        binding.apply {
            btnAddNews.setOnSingClickListener {
                findNavController().navigate(
                    R.id.action_managePostsFragment_to_postNewsFragment,
                    null,
                    NavOptions.Builder().setPopUpTo(R.id.managePostsFragment, true).build()
                )
            }
        }
    }
}