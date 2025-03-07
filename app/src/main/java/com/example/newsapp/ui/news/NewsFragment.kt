package com.example.newsapp.ui.news

import android.annotation.SuppressLint
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.data.response.NewsResponse
import com.example.newsapp.databinding.FragmentNewsBinding
import com.example.newsapp.utils.Logger
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment(private val categoryName: String?) : Fragment() {
    private lateinit var binding: FragmentNewsBinding
    private val viewModel by viewModels<NewsViewModel>()
    private lateinit var adapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsBinding.inflate(layoutInflater)
        setupUI()
        setupObserver()
        setOnClickNews()
        return binding.root
    }

    private fun setupUI() {
        binding.apply {
            adapter = NewsAdapter(mutableListOf())
            rvNews.layoutManager = LinearLayoutManager(requireContext())
            rvNews.addItemDecoration(
                DividerItemDecoration(
                    rvNews.context, (rvNews.layoutManager as LinearLayoutManager).orientation
                )
            )
            rvNews.adapter = adapter
        }
    }

    private fun setupObserver() {
        binding.apply {
            with(viewModel) {
                getData(categoryName)
                newsResult.observe(viewLifecycleOwner) { resource ->
                    resource?.data.let {
                        it?.result?.let { news ->
                            Logger.logI("LIST NEWS: $news")
                            adapter.addData(news)
                        }
                    }
                }
            }
        }
    }

    private fun setOnClickNews() {
        adapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(R.id.action_newsFragment_to_articlesFragment)
        }
    }
}