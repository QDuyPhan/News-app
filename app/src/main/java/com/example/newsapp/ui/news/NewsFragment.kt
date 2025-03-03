package com.example.newsapp.ui.news

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.newsapp.databinding.FragmentNewsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment(private val categoryName: String) : Fragment() {
    private lateinit var binding: FragmentNewsBinding
    private val viewModel by viewModels<NewsViewModel>()
    private val adapter by lazy { NewsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsBinding.inflate(layoutInflater)
        setupObserver()
        return binding.root
    }

    private fun setupObserver() {
        binding.apply {
            rvNews.adapter = adapter
            with(viewModel) {
                getData(categoryName)
                newsResult.observe(viewLifecycleOwner) { resource ->
                    resource?.data.let {
                        adapter.list = it?.result!!
                    }
                }
            }
        }

    }

}