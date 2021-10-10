package com.naman.news.api

import com.naman.news.models.NewsResponse
import com.naman.news.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode:String="us",
        @Query("page")
        pageNumber:Int=1,
        @Query("apiKey")
        apiKey:String=API_KEY

    ):Response<NewsResponse>
}