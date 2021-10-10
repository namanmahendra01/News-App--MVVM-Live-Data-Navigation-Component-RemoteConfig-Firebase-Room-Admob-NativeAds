package com.naman.news.repository

import android.content.Context
import com.naman.news.DBroom.ArticleDatabase
import com.naman.news.api.RetrofitInstance
import com.naman.news.models.Article
import com.naman.news.ui.NewsActivity

class NewsRepository(val db: ArticleDatabase, newsActivity: NewsActivity) {

    val sharedPreference =  newsActivity.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
   val cc= sharedPreference.getString("cc","us");

    suspend fun getBreakingNews(countryCodeValue:String,pageNumber:Int)=
        cc?.let { RetrofitInstance.api.getBreakingNews(it,pageNumber) }


    suspend fun insert(article:Article)=db.getArticleDao().insert(article)

    suspend fun deleteArticles(article: Article)=db.getArticleDao().deleteArticle(article)
}