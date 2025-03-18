package com.example.newsapp.ui.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.data.local.entity.NewsEntity
import com.example.newsapp.data.remote.response.UserResponse
import com.example.newsapp.databinding.FragmentNewsBinding
import com.example.newsapp.ui.home.HomeFragmentDirections
import com.example.newsapp.utils.Logger
import com.example.newsapp.utils.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment : Fragment() {
    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<NewsViewModel>()
    private lateinit var adapter: NewsAdapter
    private var categoryName: String? = null
    private var user: UserResponse? = null

    companion object {
        fun newInstance(categoryName: String): NewsFragment {
            val fragment = NewsFragment()
            val args = Bundle()
            args.putString("category_name", categoryName)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            categoryName = it.getString("category_name")  // Nhận giá trị từ Bundle
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupObserver()
        getUserId()
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

    private fun getUserId() {
        viewModel.user.observe(viewLifecycleOwner) { user ->
            this.user = user
        }
    }

    private fun setupObserver() {
        binding.apply {
            with(viewModel) {
                getData(categoryName)
                newsResult.observe(viewLifecycleOwner) { resource ->
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
            viewModel.saveReadNews(
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
            val action = HomeFragmentDirections.actionHomeFragmentToArticlesFragment(
                article = it,
                savedArticle = null,
                previousScreen = "news"
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