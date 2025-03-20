package com.example.newsapp.ui.summary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.data.local.entity.NewsEntity
import com.example.newsapp.data.remote.response.NewsResponse
import com.example.newsapp.data.remote.response.UserResponse
import com.example.newsapp.databinding.FragmentSummaryBinding
import com.example.newsapp.ui.news.NewsAdapter
import com.example.newsapp.ui.news.NewsViewModel
import com.example.newsapp.utils.Logger
import com.example.newsapp.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class SummaryFragment : Fragment() {
    private var _binding: FragmentSummaryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: NewsAdapter
    private val summaryViewModel by viewModels<SummaryViewModel>()
    private val newsViewModel by viewModels<NewsViewModel>()
    private var user: UserResponse? = null
    private var listNews: List<NewsResponse> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSummaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupObserver()
        setOnClickNews()
        getUserId()
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
        if (keySearch != null) {
            val filteredList = ArrayList<NewsResponse>()
            val searchKey = keySearch.lowercase(Locale.ROOT)

            for (i in listNews) {
                if (i.title.lowercase(Locale.ROOT).contains(searchKey)) {
                    filteredList.add(i)
                }
            }

            adapter.addData(filteredList)
        }
    }


    private fun setupObserver() {
        binding.apply {
            with(summaryViewModel) {
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
                                    listNews = news
                                    Logger.logI("List news SummaryFragment: $listNews")
                                }
                            }
                            rvNews.visibility = View.VISIBLE
                        }
                    }

                }
            }
        }
    }

    private fun getUserId() {
        newsViewModel.user.observe(viewLifecycleOwner) { user ->
            this.user = user
        }
    }

    private fun setOnClickNews() {
        adapter.setOnItemClickListener {
            newsViewModel.saveReadNews(
                NewsEntity(
                    it.id,
                    it.title,
                    it.content,
                    it.image,
                    it.publishedAt,
                    it.createdAt,
                    it.updatedAt,
                    it.categories,
                    user?.id.toString()
                )
            )
            val action = SummaryFragmentDirections.actionSummaryFragmentToArticlesFragment(
                article = it, savedArticle = null, previousScreen = "summary"
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