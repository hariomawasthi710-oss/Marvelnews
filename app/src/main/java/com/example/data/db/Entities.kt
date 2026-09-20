package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profiles")
data class UserProfileEntity(
    @PrimaryKey val email: String,
    val coinsBalance: Int = 0,
    val lifetimeCoins: Int = 0,
    val totalWithdrawnInr: Double = 0.0
)

@Entity(tableName = "daily_earnings")
data class DailyEarningEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val email: String,
    val dateString: String, // Format: YYYY-MM-DD
    val coinsEarnedToday: Int = 0,
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "coin_transactions")
data class CoinTransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val email: String,
    val type: String, // READING_REWARD, UPI_WITHDRAWAL, SHOPPING_VOUCHER, BONUS_TEST
    val coins: Int, // positive for earn, negative for debit
    val inrAmount: Double,
    val note: String,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "SUCCESS",
    val referenceDetails: String = ""
)

@Entity(tableName = "saved_articles")
data class SavedArticleEntity(
    @PrimaryKey val articleId: String,
    val savedTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "published_articles")
data class PublishedArticleEntity(
    @PrimaryKey val id: String,
    val title: String,
    val summary: String,
    val contentJoined: String, // Delimited by "\n\n"
    val bulletPointsJoined: String, // Delimited by "\n"
    val categoryId: String,
    val publishedTime: String,
    val readTimeMinutes: Int = 3,
    val author: String,
    val imageUrl: String,
    val city: String?,
    val isBreaking: Boolean = false,
    val viewCount: String = "1.5K",
    val publisherEmail: String = "hariomawasthi710@gmail.com",
    val publishedTimestamp: Long = System.currentTimeMillis()
)
