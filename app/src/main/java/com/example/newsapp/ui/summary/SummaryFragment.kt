package com.example.newsapp.ui.summary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.databinding.FragmentSummaryBinding
import com.example.newsapp.ui.news.NewsAdapter
import com.example.newsapp.utils.Logger
import com.example.newsapp.utils.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SummaryFragment : Fragment() {
    private var _binding: FragmentSummaryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: NewsAdapter
    private val viewModel by viewModels<SummaryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSummaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupObserver()
        setOnClickNews()
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
                news.observe(viewLifecycleOwner) { resource ->
                    when (resource.status) {
                        Status.LOADING -> {
                            Logger.logI("Loading...")
                            prgBarMovies.visibility = View.VISIBLE
                            rvNews.visibility = View.GONE
                        }

                        Status.ERROR -> {
                            prgBarMovies.visibility = View.GONE
                            val error = resource.message ?: "Unknown error"
                            Logger.logE(error)
                        }

                        Status.SUCCESS -> {
                            prgBarMovies.visibility = View.GONE
                            resource?.data.let {
                                it?.result?.let { news ->
                                    adapter.addData(news)
                                }
                            }
                            rvNews.visibility = View.VISIBLE
                        }
                    }

                }
            }
        }
    }

    private fun setOnClickNews() {
        adapter.setOnItemClickListener {
            val action = SummaryFragmentDirections.actionSummaryFragmentToArticlesFragment(
                article = it,
                savedArticle = null,
                previousScreen = "summary"
            )
            findNavController().navigate(
                action
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}