package com.example.newsapp.ui.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.databinding.FragmentSavedBinding
import com.example.newsapp.ui.base.BaseFragment
import com.example.newsapp.utils.Logger
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedFragment : BaseFragment<FragmentSavedBinding>() {
    private val viewModel by viewModels<SavedViewModel>()
    private lateinit var adapter: SavedNewsAdapter
    private var userId: String? = null
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSavedBinding {
        return FragmentSavedBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        getSavedNews()
        setupObserver()
        setOnClickNews()
        clickDeleteNews()
    }

    private fun setupUI() {
        binding.apply {
            adapter = SavedNewsAdapter()
            rvSavedNews.layoutManager = LinearLayoutManager(requireContext())
            rvSavedNews.addItemDecoration(
                DividerItemDecoration(
                    rvSavedNews.context,
                    (rvSavedNews.layoutManager as LinearLayoutManager).orientation
                )
            )
            rvSavedNews.adapter = adapter
        }
    }

    private fun setupObserver() {
        binding.apply {
            observeResource(
                liveData = viewModel.newsSaved,
                onSuccess = {
                    prgBarMovies.visibility = View.GONE
                    if (it.isNotEmpty()) {
                        rvSavedNews.visibility = View.VISIBLE
                        adapter.addData(it)
                    } else {
                        rvSavedNews.visibility = View.GONE
                    }
                },
                onError = {
                    prgBarMovies.visibility = View.GONE
                    val error = it
                    Logger.logE(error)
                }, onLoading = {
                    prgBarMovies.visibility = View.VISIBLE
                    rvSavedNews.visibility = View.GONE
                }
            )
        }
    }

    private fun clickDeleteNews() {
        binding.apply {
            actionBar.setOnClickRight {
                with(viewModel) {
                    deleteAllNews()
                    getSavedNews()
                }
            }
        }
    }

    private fun getSavedNews() {
        with(viewModel) {
            user.observe(viewLifecycleOwner) { user ->
                getSavedNews(user?.id.toString())
            }
        }
    }

    private fun setOnClickNews() {
        adapter.setOnItemClickListener {
            val action = SavedFragmentDirections.actionSavedFragmentToArticlesFragment(
                article = null,
                savedArticle = it,
                previousScreen = "saved"
            )
            findNavController().navigate(
                action
            )
        }
    }
}