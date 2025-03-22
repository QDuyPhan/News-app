package com.example.newsapp.ui.articles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentArticlesBinding
import com.example.newsapp.ui.base.BaseFragment
import com.example.newsapp.utils.Logger
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticlesFragment : BaseFragment<FragmentArticlesBinding>() {
    private val viewModel by viewModels<ArticlesViewModel>()

    private val args: ArticlesFragmentArgs by navArgs()
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentArticlesBinding {
        return FragmentArticlesBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val article = args.article
        val savedArticle = args.savedArticle
        val previousScreen = args.previousScreen
        val link = article?.content ?: savedArticle?.content
        link?.let {
            loadURLToWebView(it)
        } ?: run {
            Logger.logI("Không tìm thấy nội dung bài viết")
        }
        clickBack(previousScreen)
    }

    private fun loadURLToWebView(link: String) {
        binding.progressBar.visibility = View.VISIBLE
        binding.webView.apply {
            webViewClient = object : WebViewClient() {
                override fun onPageCommitVisible(view: WebView?, url: String?) {
                    super.onPageCommitVisible(view, url)
                    binding.progressBar.visibility = View.GONE
                }
            }
            loadUrl(link)
        }
    }

    private fun clickBack(previousScreen: String?) {
        binding.apply {
            actionBar.setOnClickLeft {
                when (previousScreen) {
                    "news" -> findNavController().navigate(R.id.action_articlesFragment_to_homeFragment)
                    "saved" -> findNavController().navigate(R.id.action_articlesFragment_to_savedFragment)
                    "summary" -> findNavController().navigate(R.id.action_articlesFragment_to_summaryFragment)
                    else -> findNavController().navigate(R.id.homeFragment)
                }

            }
        }
    }
}