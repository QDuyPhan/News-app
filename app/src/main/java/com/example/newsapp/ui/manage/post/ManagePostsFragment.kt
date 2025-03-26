package com.example.newsapp.ui.manage.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.newsapp.adapter.ViewPagerAdapter
import com.example.newsapp.data.remote.response.CategoryResponse
import com.example.newsapp.databinding.FragmentManagePostsBinding
import com.example.newsapp.ui.base.BaseFragment
import com.example.newsapp.ui.home.HomeViewModel
import com.example.newsapp.utils.Logger
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManagePostsFragment : BaseFragment<FragmentManagePostsBinding>() {
    private val managePostsViewModel by viewModels<ManagePostsViewModel>()
    private val homeViewModel by viewModels<HomeViewModel>()
    private var listCategory: List<CategoryResponse>? = null
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun inflateBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentManagePostsBinding {
        return FragmentManagePostsBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupData()
        clickBack()
    }

    private fun setupData() {
        observeResource(liveData = homeViewModel.listCategory, onSuccess = {
            listCategory = it.result
            val fragmentManager: FragmentManager = childFragmentManager
            viewPagerAdapter = ViewPagerAdapter(
                fragmentManager, lifecycle, listCategory, previousScreen = "manage"
            )
            binding.viewPager2.adapter = viewPagerAdapter

            TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
                tab.text =
                    (if (position < listCategory?.size!!) listCategory?.get(position)?.description else null)

            }.attach()

            binding.actionBar.setOnClickRight {
                val fragments = viewPagerAdapter.getFragments()
                fragments.forEach { fragment ->
                    fragment.toggleDeleteModeOrPerformDelete()
                }
            }
        }, onError = {
            val error = it
            Logger.logE(error)
        }, onLoading = {
            Logger.logI("Loading...")
        })
    }

    private fun setupUI() {
        binding.apply {
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    searchNews(newText)
                    return true
                }

            })
        }
    }

    private fun searchNews(keySearch: String?) {
        val currentPosition = binding.viewPager2.currentItem
        val fragment = viewPagerAdapter.getFragments().getOrNull(currentPosition)
        fragment?.searchNews(keySearch ?: "")
    }

    private fun clickBack() {
        binding.apply {
            actionBar.setOnClickLeft {
                findNavController().popBackStack()
            }
        }
    }
}