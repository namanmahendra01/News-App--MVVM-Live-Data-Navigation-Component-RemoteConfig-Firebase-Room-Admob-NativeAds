package com.naman.news.ui

import android.app.Application
import android.content.Context
import android.content.Context.TELEPHONY_SERVICE
import android.content.pm.ApplicationInfo
import android.telephony.TelephonyManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naman.news.models.Article
import com.naman.news.models.NewsResponse
import com.naman.news.repository.NewsRepository
import com.naman.news.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(val newsRepository: NewsRepository) : ViewModel() {

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1
    var breakingNewsResponse: NewsResponse? = null

    init {
        getBreakingNews("us")
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {

        breakingNews.postValue(Resource.Loading())
        val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
        breakingNews.postValue(response?.let { handleBreakingNewsResponse(it) })

    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {

                breakingNewsPage++;
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = it
                } else {
                    val oArticles = breakingNewsResponse?.articles
                    val nArticles = it.articles
                    oArticles?.addAll(nArticles)
                }
                return Resource.Success(breakingNewsResponse ?: it)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.insert(article)
    }

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticles(article)
    }
}