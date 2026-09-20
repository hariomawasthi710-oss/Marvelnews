package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.NewsArticle
import com.example.ui.theme.BreakingBannerBg
import com.example.ui.theme.CrimsonLight
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.GoldCoin
import com.example.ui.theme.GoldCoinDark
import com.example.ui.theme.GoldCoinLight
import com.example.ui.theme.NeutralDark
import com.example.ui.theme.NeutralMedium
import com.example.ui.theme.SaffronOrange

@Composable
fun BreakingNewsTickerRow(
    breakingText: String,
    onClick: () -> Unit
) {
    Surface(
        color = CrimsonLight,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = BreakingBannerBg,
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.FiberManualRecord,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(8.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "BREAKING",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(
                text = breakingText,
                color = CrimsonRed,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun EarningBannerCard(
    coinsToday: Int,
    maxCoins: Int,
    onOpenWallet: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 6.dp)
            .clickable { onOpenWallet() }
            .testTag("earning_banner_card"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = GoldCoinLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(GoldCoin),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MonetizationOn,
                    contentDescription = "Coin Reward",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Earn 1 Coin for every 2 mins of reading! 🪙",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeutralDark
                )
                Text(
                    text = "Today's Earned: $coinsToday / $maxCoins Coins • 10,000 Coins = ₹5.00",
                    fontSize = 11.sp,
                    color = NeutralMedium
                )
            }

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = SaffronOrange,
                modifier = Modifier.padding(start = 4.dp)
            ) {
                Text(
                    text = "View Wallet",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun HeroArticleCard(
    article: NewsArticle,
    isBookmarked: Boolean,
    onArticleClick: () -> Unit,
    onBookmarkClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .clickable { onArticleClick() }
            .testTag("hero_article_${article.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column {
            // Image with Overlays
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                AsyncImage(
                    model = article.imageUrl,
                    contentDescription = article.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Gradient Dark Scrim at bottom
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f)),
                                startY = 100f
                            )
                        )
                )

                // Badges top row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (article.isOwnerPost) GoldCoinDark else if (article.isBreaking) BreakingBannerBg else SaffronOrange
                    ) {
                        Text(
                            text = if (article.isOwnerPost) "👑 OWNER UPDATE" else if (article.isBreaking) "BREAKING" else article.category.titleEnglish.uppercase(),
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    // 2-min reward badge
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.Black.copy(alpha = 0.65f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = null,
                                tint = GoldCoin,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "2 mins = 1 🪙",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                // Bookmark icon button bottom right of image
                IconButton(
                    onClick = onBookmarkClick,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = if (isBookmarked) GoldCoin else Color.White
                    )
                }
            }

            // Text info
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = article.title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = article.summary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color = NeutralMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Footer Metadata
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = article.author,
                            fontSize = 11.sp,
                            color = NeutralMedium,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = " • ${article.publishedTime}",
                            fontSize = 11.sp,
                            color = NeutralMedium
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = null,
                            tint = NeutralMedium,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "${article.viewCount} views",
                            fontSize = 11.sp,
                            color = NeutralMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StandardArticleCard(
    article: NewsArticle,
    isBookmarked: Boolean,
    onArticleClick: () -> Unit,
    onBookmarkClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 6.dp)
            .clickable { onArticleClick() }
            .testTag("article_card_${article.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left content
            Column(modifier = Modifier.weight(1f)) {
                // Category Tag + Reward hint
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 4.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = if (article.isOwnerPost) GoldCoinDark else CrimsonLight
                    ) {
                        Text(
                            text = if (article.isOwnerPost) "👑 OWNER UPDATE" else article.category.titleEnglish,
                            color = if (article.isOwnerPost) Color.White else CrimsonRed,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = "⏱️ 2 mins = 1 🪙",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = GoldCoinDark
                    )
                }

                // Title
                Text(
                    text = article.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 19.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Time & Views
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = article.publishedTime,
                        fontSize = 11.sp,
                        color = NeutralMedium
                    )
                    Text(
                        text = " • ${article.viewCount} views",
                        fontSize = 11.sp,
                        color = NeutralMedium
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Right image thumbnail
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                AsyncImage(
                    model = article.imageUrl,
                    contentDescription = article.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Bookmark icon overlay
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.4f))
                        .size(26.dp)
                        .clickable { onBookmarkClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = if (isBookmarked) GoldCoin else Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
