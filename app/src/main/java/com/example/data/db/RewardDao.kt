package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RewardDao {
    @Query("SELECT * FROM user_profiles WHERE email = :email LIMIT 1")
    fun observeUserProfile(email: String): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profiles WHERE email = :email LIMIT 1")
    suspend fun getUserProfile(email: String): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserProfile(profile: UserProfileEntity)

    @Query("SELECT * FROM daily_earnings WHERE email = :email AND dateString = :dateString LIMIT 1")
    fun observeDailyEarning(email: String, dateString: String): Flow<DailyEarningEntity?>

    @Query("SELECT * FROM daily_earnings WHERE email = :email AND dateString = :dateString LIMIT 1")
    suspend fun getDailyEarning(email: String, dateString: String): DailyEarningEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDailyEarning(daily: DailyEarningEntity)

    @Query("SELECT * FROM coin_transactions WHERE email = :email ORDER BY timestamp DESC")
    fun observeTransactions(email: String): Flow<List<CoinTransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: CoinTransactionEntity)
}

@Dao
interface SavedArticleDao {
    @Query("SELECT articleId FROM saved_articles")
    fun observeSavedArticleIds(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun bookmarkArticle(saved: SavedArticleEntity)

    @Query("DELETE FROM saved_articles WHERE articleId = :articleId")
    suspend fun removeBookmark(articleId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM saved_articles WHERE articleId = :articleId)")
    suspend fun isBookmarked(articleId: String): Boolean
}
