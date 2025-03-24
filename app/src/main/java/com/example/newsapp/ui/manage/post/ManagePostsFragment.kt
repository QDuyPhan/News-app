package com.example.newsapp.ui.manage.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.data.local.entity.NewsItem
import com.example.newsapp.data.remote.response.UserResponse
import com.example.newsapp.databinding.FragmentManagePostsBinding
import com.example.newsapp.ui.base.BaseFragment
import com.example.newsapp.ui.news.NewsViewModel
import com.example.newsapp.ui.summary.SummaryViewModel
import com.example.newsapp.utils.Logger
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class ManagePostsFragment : BaseFragment<FragmentManagePostsBinding>() {
    private val managePostsViewModel by viewModels<ManagePostsViewModel>()
    private val summaryViewModel by viewModels<SummaryViewModel>()
    private val newsViewModel by viewModels<NewsViewModel>()
    private var listNews: List<NewsItem> = emptyList()
    private var user: UserResponse? = null
    private lateinit var adapter: NewsAdapter
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentManagePostsBinding {
        return FragmentManagePostsBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

            // ✅ Xử lý nút xóa
//            btnDeleteSelected.setOnClickListener {
//                adapter.deleteSelectedItems()
//            }
        }
    }

    private fun searchNews(keySearch: String?) {
        if (keySearch != null) {
            val searchKey = keySearch.lowercase(Locale.ROOT)
            val filteredList = listNews.filter {
                it.news.title.lowercase(Locale.ROOT).contains(searchKey)
            }
            adapter.setData(filteredList)
        }
    }

    private fun setupObserver() {
        binding.apply {
            with(summaryViewModel) {
                observeResource(
                    liveData = news,
                    onSuccess = {
                        prgBarMovies.visibility = View.GONE
                        it.result.let { news ->
//                            adapter.setData(news)
//                            listNews = news
                            Logger.logI("List news SummaryFragment: $listNews")
                        }
                        rvNews.visibility = View.VISIBLE
                    },
                    onError = {
                        prgBarMovies.visibility = View.GONE
                        val error = it
                        Logger.logE(error)
                    },
                    onLoading = {
                        Logger.logI("Loading...")
                        prgBarMovies.visibility = View.VISIBLE
                        rvNews.visibility = View.GONE
                    }
                )
            }
        }
    }

    private fun getUserId() {
        newsViewModel.user.observe(viewLifecycleOwner) { user ->
            this.user = user
        }
    }


}