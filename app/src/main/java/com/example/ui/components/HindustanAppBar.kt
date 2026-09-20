package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NewsCategory
import com.example.data.model.RewardUser
import com.example.ui.theme.CrimsonDark
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.GoldCoin

@Composable
fun HindustanAppBar(
    selectedCategory: NewsCategory,
    onCategorySelected: (NewsCategory) -> Unit,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    rewardUser: RewardUser,
    isOwner: Boolean = false,
    onOpenPublishStudio: () -> Unit,
    onOpenWallet: () -> Unit,
    onOpenBookmarks: () -> Unit,
    onOpenProfile: () -> Unit
) {
    var isSearchActive by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(CrimsonDark, CrimsonRed)
                )
            )
    ) {
        // Date and Location Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Saturday, Sep 20, 2026 | Global Edition",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "e-Paper Edition",
                color = GoldCoin,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Main Masthead Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Brand Title
            Column(
                modifier = Modifier.clickable { onCategorySelected(NewsCategory.HOME) }
            ) {
                Text(
                    text = "MARVEL NEWS",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.5.sp,
                    fontFamily = FontFamily.SansSerif
                )
                Text(
                    text = "THE PULSE OF TRUTH & INSIGHT",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            // Quick Actions: Coin Pill & Profile
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Live Coin & Rupee Badge Pill
                Surface(
                    onClick = onOpenWallet,
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White.copy(alpha = 0.18f),
                    modifier = Modifier
                        .padding(end = 6.dp)
                        .testTag("wallet_badge_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.MonetizationOn,
                            contentDescription = "Wallet Coins",
                            tint = GoldCoin,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${rewardUser.coinBalance}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = " (₹${"%.2f".format(rewardUser.inrValue)})",
                            color = GoldCoin,
                            fontWeight = FontWeight.Medium,
                            fontSize = 11.sp
                        )
                    }
                }

                // Search Icon Button
                IconButton(
                    onClick = { isSearchActive = !isSearchActive },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = if (isSearchActive) Icons.Default.Close else Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Bookmarks Button
                IconButton(
                    onClick = onOpenBookmarks,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Bookmark,
                        contentDescription = "Saved Bookmarks",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Owner Studio / Publish Button
                IconButton(
                    onClick = onOpenPublishStudio,
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("appbar_publish_studio_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Campaign,
                        contentDescription = "Owner Studio - Publish Update",
                        tint = if (isOwner) GoldCoin else Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Profile / Email Switch Button
                IconButton(
                    onClick = onOpenProfile,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "User Account",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }

        // Search Bar (Expanded)
        AnimatedVisibility(visible = isSearchActive) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChanged,
                    placeholder = { Text("Search news (e.g. Marvel, Markets, AI)...", fontSize = 13.sp) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White),
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchQueryChanged("") }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear search")
                            }
                        }
                    }
                )
            }
        }

        // Navigation Categories Bar (All required categories in English)
        ScrollableTabRow(
            selectedTabIndex = NewsCategory.entries.indexOf(selectedCategory).coerceAtLeast(0),
            containerColor = Color.Transparent,
            contentColor = Color.White,
            edgePadding = 12.dp,
            divider = {},
            indicator = { tabPositions ->
                val index = NewsCategory.entries.indexOf(selectedCategory).coerceAtLeast(0)
                if (index < tabPositions.size) {
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[index]),
                        color = GoldCoin,
                        height = 3.5.dp
                    )
                }
            }
        ) {
            NewsCategory.entries.forEach { category ->
                val isSelected = selectedCategory == category
                Tab(
                    selected = isSelected,
                    onClick = { onCategorySelected(category) },
                    modifier = Modifier.testTag("tab_${category.id}"),
                    text = {
                        Text(
                            text = category.titleEnglish,
                            fontSize = 14.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.8f)
                        )
                    }
                )
            }
        }
    }
}
