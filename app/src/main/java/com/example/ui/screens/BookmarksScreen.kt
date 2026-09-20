package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NewsArticle
import com.example.ui.components.StandardArticleCard
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.NeutralMedium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarksScreen(
    savedArticles: List<NewsArticle>,
    onBackClick: () -> Unit,
    onArticleClick: (String) -> Unit,
    onBookmarkClick: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Saved News Articles",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick, modifier = Modifier.testTag("bookmarks_back_button")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CrimsonRed)
            )
        }
    ) { innerPadding ->
        if (savedArticles.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.BookmarkBorder,
                    contentDescription = null,
                    tint = NeutralMedium,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                Text(
                    text = "No saved articles yet",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Tap the bookmark icon on any article to save it for offline reading.",
                    fontSize = 13.sp,
                    color = NeutralMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .testTag("bookmarks_lazy_column"),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                items(savedArticles, key = { it.id }) { article ->
                    StandardArticleCard(
                        article = article,
                        isBookmarked = true,
                        onArticleClick = { onArticleClick(article.id) },
                        onBookmarkClick = { onBookmarkClick(article.id) }
                    )
                }
            }
        }
    }
}
