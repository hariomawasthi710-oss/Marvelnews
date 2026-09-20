package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PublishedArticleDao {
    @Query("SELECT * FROM published_articles ORDER BY publishedTimestamp DESC")
    fun observePublishedArticles(): Flow<List<PublishedArticleEntity>>

    @Query("SELECT * FROM published_articles ORDER BY publishedTimestamp DESC")
    suspend fun getAllPublishedArticles(): List<PublishedArticleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPublishedArticle(article: PublishedArticleEntity)

    @Query("DELETE FROM published_articles WHERE id = :id")
    suspend fun deletePublishedArticle(id: String)
}
