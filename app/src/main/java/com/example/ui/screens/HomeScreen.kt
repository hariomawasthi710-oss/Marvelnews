package com.example.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NewsArticle
import com.example.data.model.NewsCategory
import com.example.data.model.RewardUser
import com.example.ui.components.BreakingNewsTickerRow
import com.example.ui.components.EarningBannerCard
import com.example.ui.components.HeroArticleCard
import com.example.ui.components.StandardArticleCard
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.GoldCoin
import com.example.ui.theme.NeutralMedium

@Composable
fun HomeScreen(
    selectedCategory: NewsCategory,
    searchQuery: String,
    articles: List<NewsArticle>,
    savedArticleIds: List<String>,
    rewardUser: RewardUser,
    isOwner: Boolean = false,
    onOpenPublishStudio: () -> Unit,
    onArticleClick: (String) -> Unit,
    onBookmarkClick: (String) -> Unit,
    onOpenWallet: () -> Unit
) {
    val breakingArticle = articles.firstOrNull { it.isBreaking } ?: articles.firstOrNull()
    val heroArticle = articles.firstOrNull()
    val listArticles = if (articles.size > 1) articles.drop(1) else articles

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("home_screen_lazy_column"),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Breaking News Alert Ticker
            if (breakingArticle != null && searchQuery.isEmpty()) {
                item {
                    BreakingNewsTickerRow(
                        breakingText = breakingArticle.title,
                        onClick = { onArticleClick(breakingArticle.id) }
                    )
                }
            }

            // 2-Minute Coin Earning Awareness Banner
            if (searchQuery.isEmpty()) {
                item {
                    EarningBannerCard(
                        coinsToday = rewardUser.coinsEarnedToday,
                        maxCoins = rewardUser.maxDailyCoins,
                        onOpenWallet = onOpenWallet
                    )
                }
            }

            // Section Title
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = if (searchQuery.isNotEmpty()) {
                            "Search results for: '$searchQuery'"
                        } else if (selectedCategory == NewsCategory.HOME) {
                            "Top Headlines & Stories"
                        } else {
                            selectedCategory.titleEnglish
                        },
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // If no articles match search
            if (articles.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.SearchOff,
                            contentDescription = null,
                            tint = NeutralMedium,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        Text(
                            text = "No articles found",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Please try searching with a different keyword",
                            color = NeutralMedium,
                            fontSize = 13.sp
                        )
                    }
                }
            } else {
                // First big hero article (if in Home and no search query)
                if (heroArticle != null && searchQuery.isEmpty()) {
                    item {
                        HeroArticleCard(
                            article = heroArticle,
                            isBookmarked = savedArticleIds.contains(heroArticle.id),
                            onArticleClick = { onArticleClick(heroArticle.id) },
                            onBookmarkClick = { onBookmarkClick(heroArticle.id) }
                        )
                    }
                }

                // Remaining standard cards
                val cardsToShow = if (searchQuery.isEmpty()) listArticles else articles
                items(cardsToShow, key = { it.id }) { article ->
                    StandardArticleCard(
                        article = article,
                        isBookmarked = savedArticleIds.contains(article.id),
                        onArticleClick = { onArticleClick(article.id) },
                        onBookmarkClick = { onBookmarkClick(article.id) }
                    )
                }
            }
        }

        // Owner Publish Action Button
        ExtendedFloatingActionButton(
            onClick = onOpenPublishStudio,
            icon = {
                Icon(
                    imageVector = Icons.Default.Campaign,
                    contentDescription = null,
                    tint = GoldCoin
                )
            },
            text = {
                Text(
                    text = if (isOwner) "👑 Publish Live Update" else "Owner Studio",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color.White
                )
            },
            containerColor = CrimsonRed,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .testTag("fab_publish_update")
        )
    }
}
