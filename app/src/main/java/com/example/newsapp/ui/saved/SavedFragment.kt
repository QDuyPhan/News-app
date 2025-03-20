package com.example.newsapp.ui.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.databinding.FragmentSavedBinding
import com.example.newsapp.utils.Logger
import com.example.newsapp.utils.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedFragment : Fragment() {
    private val viewModel by viewModels<SavedViewModel>()
    private var _binding: FragmentSavedBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: SavedNewsAdapter
    private var userId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedBinding.inflate(layoutInflater)
        return binding.root
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
            with(viewModel) {
                newsSaved.observe(viewLifecycleOwner) { resources ->
                    when (resources.status) {
                        Status.LOADING -> {
                            prgBarMovies.visibility = View.VISIBLE
                            rvSavedNews.visibility = View.GONE
                        }

                        Status.ERROR -> {
                            prgBarMovies.visibility = View.GONE
                            val error = resources.message ?: "Unknown error"
                            Logger.logE(error)
                        }

                        Status.SUCCESS -> {
                            prgBarMovies.visibility = View.GONE
                            resources.data?.let { news ->
                                if (news.isNotEmpty()) {
                                    rvSavedNews.visibility = View.VISIBLE
                                    adapter.addData(news)
                                } else {
                                    rvSavedNews.visibility = View.GONE
                                }
                            }
                        }
                    }
                }
            }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}