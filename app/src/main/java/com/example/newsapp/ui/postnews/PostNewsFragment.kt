package com.example.newsapp.ui.postnews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.example.newsapp.data.remote.response.CategoryResponse
import com.example.newsapp.databinding.FragmentPostNewsBinding
import com.example.newsapp.ui.base.BaseFragment
import com.example.newsapp.ui.home.HomeViewModel
import com.example.newsapp.utils.Logger
import com.example.newsapp.utils.setOnSingClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostNewsFragment : BaseFragment<FragmentPostNewsBinding>() {
    private val postNewsViewModel by viewModels<PostNewsViewModel>()
    private val homeViewModel by viewModels<HomeViewModel>()
    private lateinit var listCategory: List<CategoryResponse>
    private lateinit var selectedCategoryId: String
    override fun inflateBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentPostNewsBinding {
        return FragmentPostNewsBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        clickPostNews()
    }

    private fun setupObserver() {
        observeResource(
            liveData = homeViewModel.listCategory,
            onSuccess = {
                listCategory = it.result
                val spinnerItems = listCategory.map {
                    it.description
                }
                val adapter = ArrayAdapter(
                    requireContext(), android.R.layout.simple_spinner_item, spinnerItems
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spCategory.adapter = adapter

                binding.spCategory.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>, view: View?, position: Int, id: Long
                        ) {
                            val selectedCategory = listCategory[position]
                            selectedCategoryId = selectedCategory.name
                            Logger.logI("Selected category ID: $selectedCategoryId")
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                        }
                    }
            },
            onError = {
                val error = it
                Logger.logE(error)
            },
            onLoading = {
                Logger.logI("Loading...")
            }
        )
    }

    private fun clickPostNews() {
        binding.btnPostNews.setOnSingClickListener {
            val title = binding.edtTitle.text.toString()
            val link = binding.edtLinkContent.text.toString()
            val img = binding.edtLinkImg.text.toString()
            postNewsViewModel.postNews(title, link, img, selectedCategoryId)
            postNews()
        }
    }

    private fun postNews() {
        observeResource(
            liveData = postNewsViewModel.postNewsResponse,
            onSuccess = {
                Logger.logI("Đăng tin tức thành công")
            },
            onError = {
                val error = it
                Logger.logE(error)
            },
            onLoading = { Logger.logI("Loading...") }
        )
    }
}