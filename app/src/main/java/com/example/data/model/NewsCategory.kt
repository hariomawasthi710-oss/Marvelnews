package com.example.data.model

enum class NewsCategory(
    val id: String,
    val titleHindi: String,
    val titleEnglish: String,
    val iconName: String
) {
    HOME("home", "होम", "Home", "Home"),
    NEWS("news", "ताज़ा समाचार", "News", "Article"),
    FINANCE("finance", "फाइनेंस", "Finance", "TrendingUp"),
    TECH("tech", "टेक", "Tech", "Computer"),
    HEALTH("health", "हेल्थ", "Health", "FitnessCenter"),
    DIGITAL_MARKETING("digital_marketing", "डिजिटल मार्केटिंग", "Digital Marketing", "Campaign"),
    TRENDING("trending", "ट्रेंडिंग", "Trending", "Whatshot");

    val displayName: String get() = titleEnglish

    companion object {
        fun fromId(id: String): NewsCategory {
            return entries.firstOrNull { it.id.equals(id, ignoreCase = true) } ?: HOME
        }
    }
}

data class NewsArticle(
    val id: String,
    val title: String,
    val summary: String,
    val content: List<String>,
    val bulletPoints: List<String> = emptyList(),
    val category: NewsCategory,
    val publishedTime: String,
    val readTimeMinutes: Int = 3,
    val author: String,
    val imageUrl: String,
    val city: String? = null,
    val isBreaking: Boolean = false,
    val viewCount: String = "12.4K",
    val isOwnerPost: Boolean = false
)
