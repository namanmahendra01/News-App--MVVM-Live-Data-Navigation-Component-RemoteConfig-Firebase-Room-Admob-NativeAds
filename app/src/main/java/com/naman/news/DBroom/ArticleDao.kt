package com.naman.news.DBroom

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.naman.news.models.Article

@Dao
interface ArticleDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(article: Article):Long

    @Query("Select * from articles")
    fun getAllArticles(): LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)
}