package com.naman.news.ui.fragments

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.naman.news.R
import com.naman.news.ui.NewsActivity
import com.naman.news.ui.NewsViewModel
import kotlinx.android.synthetic.main.fragment_article.*

class ArticleFragment :Fragment(R.layout.fragment_article){

lateinit var viewModel:NewsViewModel
val args:ArticleFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel=(activity as NewsActivity).viewModel

        val article=args.article

        webView.apply {
            webViewClient = WebViewClient()
            article.url?.let { loadUrl(it) }
        }

    }
}