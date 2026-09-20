package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.NewsArticle
import com.example.ui.ReadingTimerState
import com.example.ui.components.ReadingTimerFloatingBar
import com.example.ui.theme.CrimsonLight
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.GoldCoin
import com.example.ui.theme.GoldCoinDark
import com.example.ui.theme.GoldCoinLight
import com.example.ui.theme.NeutralDark
import com.example.ui.theme.NeutralMedium
import com.example.ui.theme.SaffronOrange
import com.example.ui.theme.SuccessGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleDetailScreen(
    article: NewsArticle,
    isBookmarked: Boolean,
    timerState: ReadingTimerState,
    coinsEarnedToday: Int,
    maxDailyCoins: Int,
    relatedArticles: List<NewsArticle>,
    onBackClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    onFastForwardTimer: () -> Unit,
    onRelatedArticleClick: (String) -> Unit
) {
    val context = LocalContext.current
    var fontScale by remember { mutableFloatStateOf(1.0f) }
    var isSpeaking by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = article.category.titleEnglish,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick, modifier = Modifier.testTag("article_back_button")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    // Font Size toggle
                    IconButton(onClick = {
                        fontScale = when (fontScale) {
                            1.0f -> 1.15f
                            1.15f -> 1.3f
                            else -> 1.0f
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.FormatSize,
                            contentDescription = "Font size",
                            tint = Color.White
                        )
                    }

                    // Bookmark toggle
                    IconButton(onClick = onBookmarkClick) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = if (isBookmarked) GoldCoin else Color.White
                        )
                    }

                    // Share button
                    IconButton(onClick = {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_SUBJECT, article.title)
                            putExtra(Intent.EXTRA_TEXT, "${article.title}\n\n${article.summary}\n\nRead on Marvel News & earn coins!")
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share Article"))
                    }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CrimsonRed
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("article_detail_column"),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // Prominent 2-Minute Reading Timer Bar
            item {
                ReadingTimerFloatingBar(
                    timerState = timerState,
                    coinsEarnedToday = coinsEarnedToday,
                    maxDailyCoins = maxDailyCoins,
                    onFastForward = onFastForwardTimer
                )
            }

            // Category tag and Date
            item {
                if (article.isOwnerPost) {
                    Surface(
                        color = GoldCoinLight,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "👑",
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "OFFICIAL UPDATE • POSTED BY OWNER",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 11.sp,
                                    color = GoldCoinDark
                                )
                                Text(
                                    text = "Published directly by Editor / App Owner to the public feed.",
                                    fontSize = 10.sp,
                                    color = NeutralDark
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = CrimsonLight,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = article.category.titleEnglish.uppercase(),
                            color = CrimsonRed,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Text(
                        text = "${article.city?.let { "$it | " } ?: ""}${article.publishedTime}",
                        fontSize = 12.sp,
                        color = NeutralMedium
                    )
                }
            }

            // Headline
            item {
                Text(
                    text = article.title,
                    fontSize = (21 * fontScale).sp,
                    fontWeight = FontWeight.Black,
                    lineHeight = (28 * fontScale).sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = FontFamily.SansSerif,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            // Author & Audio Listen Row
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Report by: ${article.author}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = NeutralMedium
                    )

                    Surface(
                        onClick = { isSpeaking = !isSpeaking },
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSpeaking) SuccessGreen else SaffronOrange
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "Audio news",
                                tint = Color.White,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isSpeaking) "Playing Audio..." else "Listen",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Main Featured Image
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(230.dp)
                        .padding(vertical = 8.dp)
                ) {
                    AsyncImage(
                        model = article.imageUrl,
                        contentDescription = article.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            // Key Highlights / Bullet Points Box
            if (article.bulletPoints.isNotEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "📌 Key Highlights:",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = CrimsonRed
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            article.bulletPoints.forEach { point ->
                                Row(modifier = Modifier.padding(vertical = 3.dp)) {
                                    Text(
                                        text = "• ",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CrimsonRed
                                    )
                                    Text(
                                        text = point,
                                        fontSize = (13 * fontScale).sp,
                                        lineHeight = (18 * fontScale).sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Article Body Paragraphs
            items(article.content) { paragraph ->
                Text(
                    text = paragraph,
                    fontSize = (15 * fontScale).sp,
                    lineHeight = (23 * fontScale).sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }

            // 2-Minute Reward reminder card at the bottom of the article
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (timerState.coinAwardedForThisSession) Color(0xFFE8F5E9) else GoldCoinLight
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (timerState.coinAwardedForThisSession) Icons.Default.CheckCircle else Icons.Default.MonetizationOn,
                            contentDescription = null,
                            tint = if (timerState.coinAwardedForThisSession) SuccessGreen else GoldCoinDark,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (timerState.coinAwardedForThisSession) {
                                    "Awesome! 1 Coin has been credited for this article."
                                } else {
                                    "Stay on this page for 2 minutes to earn 1 Coin."
                                },
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = if (timerState.coinAwardedForThisSession) SuccessGreen else NeutralDark
                            )
                            Text(
                                text = "Reach 10,000 Coins to withdraw ₹5.00 INR directly via UPI. (Daily Limit: 10 Coins)",
                                fontSize = 11.sp,
                                color = NeutralMedium
                            )
                        }
                    }
                }
            }

            // Related News Section
            if (relatedArticles.isNotEmpty()) {
                item {
                    Text(
                        text = "Related Stories",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                    )
                }

                item {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 14.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(relatedArticles.take(4)) { related ->
                            Card(
                                modifier = Modifier
                                    .width(220.dp)
                                    .clickable { onRelatedArticleClick(related.id) },
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                            ) {
                                Column {
                                    AsyncImage(
                                        model = related.imageUrl,
                                        contentDescription = related.title,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(110.dp)
                                    )
                                    Text(
                                        text = related.title,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        maxLines = 2,
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
