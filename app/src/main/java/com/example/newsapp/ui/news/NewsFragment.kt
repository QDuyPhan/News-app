package com.example.newsapp.ui.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.data.local.entity.NewsEntity
import com.example.newsapp.data.remote.response.UserResponse
import com.example.newsapp.databinding.FragmentNewsBinding
import com.example.newsapp.ui.base.BaseFragment
import com.example.newsapp.ui.home.HomeFragmentDirections
import com.example.newsapp.ui.manage.post.ManagePostsFragmentDirections
import com.example.newsapp.ui.widget.CustomToast
import com.example.newsapp.utils.Logger
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class NewsFragment : BaseFragment<FragmentNewsBinding>() {
    private val newsViewModel by viewModels<NewsViewModel>()
    private lateinit var adapter: NewsAdapter
    private var categoryName: String? = null
    private var previousScreen: String? = null
    private var user: UserResponse? = null

    companion object {
        fun newInstance(categoryName: String, previousScreen: String): NewsFragment {
            val fragment = NewsFragment()
            val args = Bundle()
            args.putString("category_name", categoryName)
            args.putString("previous_screen", previousScreen)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            categoryName = it.getString("category_name")
            previousScreen = it.getString("previous_screen")
        }
    }

    override fun inflateBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentNewsBinding {
        return FragmentNewsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsViewModel.getData(categoryName)
        setupUI()
        setupObserver()
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
            setOnClickNews()
        }
    }

    private fun getUserId() {
        newsViewModel.user.observe(viewLifecycleOwner) { user ->
            this.user = user
        }
    }

    private fun setupObserver() {
        binding.apply {
            observeResource(liveData = newsViewModel.newsResult, onSuccess = {
                prgBarMovies.visibility = View.GONE
                it.result.let { news ->
                    adapter.addData(news)
                }
                rvNews.visibility = View.VISIBLE
            }, onError = {
                prgBarMovies.visibility = View.GONE
                val error = it
                Logger.logE(error)
            }, onLoading = {
                Logger.logI("Loading...")
                prgBarMovies.visibility = View.VISIBLE
                rvNews.visibility = View.GONE
            })
        }
    }

    fun toggleDeleteModeOrPerformDelete() {
        if (!adapter.isSelectMode) {
            adapter.enableSelectionMode(true)
            CustomToast.makeText(
                requireContext(),
                "Chọn bài viết để xóa",
                CustomToast.LONG_DURATION,
                CustomToast.SUCCESS,
                R.drawable.baseline_info_outline_24
            ).show()
        } else {
            val selectedItems = adapter.getSelectedItems()
            if (selectedItems.isEmpty()) {
                adapter.enableSelectionMode(false)
                return
            }

            selectedItems.forEach { item ->
                Logger.logI("Đã xóa: ${item.title + item.id}")
                newsViewModel.deleteNews(item.id)
                observeResource(newsViewModel.deleteResult, onSuccess = {
                    val updatedList = adapter.getCurrentList().filter { it.id != item.id }
                    adapter.addData(updatedList)
                    CustomToast.makeText(
                        requireContext(),
                        "Xóa bài viết thành công",
                        CustomToast.LONG_DURATION,
                        CustomToast.SUCCESS,
                        R.drawable.check_icon
                    ).show()
                }, onError = {
                    Logger.logE(it)
                    CustomToast.makeText(
                        requireContext(),
                        it,
                        CustomToast.LONG_DURATION,
                        CustomToast.ERROR,
                        R.drawable.error_icon
                    ).show()
                }, onLoading = {})
            }
            adapter.enableSelectionMode(false)
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
            when (previousScreen) {
                "home" -> {
                    val action = HomeFragmentDirections.actionHomeFragmentToArticlesFragment(
                        article = it, savedArticle = null, previousScreen = "news"
                    )
                    findNavController().navigate(action)
                }

                "manage" -> {
                    val action =
                        ManagePostsFragmentDirections.actionManagePostsFragmentToArticlesFragment(
                            article = it, savedArticle = null, previousScreen = "manage_news"
                        )
                    findNavController().navigate(action)
                }

                else -> {
                    Logger.logE("Unknown previous screen")
                }
            }
        }
    }

    fun searchNews(keySearch: String) {
        val currentList = newsViewModel.newsResult.value?.data?.result ?: return
        if (keySearch.isEmpty()) {
            adapter.addData(currentList)
        } else {
            val filteredList = currentList.filter {
                it.title.lowercase(Locale.ROOT).contains(keySearch.lowercase(Locale.ROOT))
            }
            adapter.addData(filteredList)
        }
    }

}