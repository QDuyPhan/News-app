package com.example.newsapp.ui.postnews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.newsapp.R
import com.example.newsapp.data.remote.response.CategoryResponse
import com.example.newsapp.databinding.FragmentPostNewsBinding
import com.example.newsapp.ui.base.BaseFragment
import com.example.newsapp.ui.home.HomeViewModel
import com.example.newsapp.ui.widget.CustomToast
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
        clickBack()
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

    private fun setEmpty() {
        binding.edtTitle.text.clear()
        binding.edtLinkContent.text.clear()
        binding.edtLinkImg.text.clear()
    }

    private fun clickBack() {
        binding.apply {
            actionBar.setOnClickLeft {
                findNavController().navigate(
                    R.id.action_postNewsFragment_to_managePostsFragment,
                    null,
                    NavOptions.Builder().setPopUpTo(R.id.postNewsFragment, true).build()
                )
            }
        }
    }

    private fun clickPostNews() {
        binding.btnPostNews.setOnSingClickListener {
            val title = binding.edtTitle.text.toString()
            val link = binding.edtLinkContent.text.toString()
            val img = binding.edtLinkImg.text.toString()
            if (title.isBlank() || link.isBlank() || img.isBlank()) {
                if (title.isBlank()) CustomToast.makeText(
                    requireContext(),
                    "Tiêu đề không được bỏ trống",
                    CustomToast.LONG_DURATION,
                    CustomToast.WARNING,
                    R.drawable.warning_icon
                ).show()
                if (link.isBlank()) CustomToast.makeText(
                    requireContext(),
                    "Đường dẫn bài viết không được bỏ trống",
                    CustomToast.LONG_DURATION,
                    CustomToast.WARNING,
                    R.drawable.warning_icon
                ).show()
                if (img.isBlank()) CustomToast.makeText(
                    requireContext(),
                    "Đường dẫn hình ảnh không đươc bỏ trống",
                    CustomToast.LONG_DURATION,
                    CustomToast.WARNING,
                    R.drawable.warning_icon
                ).show()

            } else {
                postNewsViewModel.postNews(title, link, img, selectedCategoryId)
                postNews()
                setEmpty()
                CustomToast.makeText(
                    requireContext(),
                    "Đăng tin tức thành công",
                    CustomToast.LONG_DURATION,
                    CustomToast.SUCCESS,
                    R.drawable.check_icon
                ).show()
            }
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