package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.NewsCategory
import com.example.ui.NewsViewModel
import com.example.ui.ScreenDestination
import com.example.ui.components.CelebrationDialog
import com.example.ui.components.EmailSwitchDialog
import com.example.ui.components.HindustanAppBar
import com.example.ui.screens.ArticleDetailScreen
import com.example.ui.screens.BookmarksScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PublishArticleScreen
import com.example.ui.screens.RewardWalletScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val viewModel: NewsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MarvelNewsAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MarvelNewsAppContent(viewModel: NewsViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val timerState by viewModel.timerState.collectAsStateWithLifecycle()
    val celebration by viewModel.celebration.collectAsStateWithLifecycle()
    val rewardUser by viewModel.rewardUser.collectAsStateWithLifecycle()
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    val savedArticleIds by viewModel.savedArticleIds.collectAsStateWithLifecycle()
    val isWithdrawing by viewModel.isWithdrawing.collectAsStateWithLifecycle()
    val upiWithdrawalMessage by viewModel.upiWithdrawalMessage.collectAsStateWithLifecycle()
    val allArticles by viewModel.allArticles.collectAsStateWithLifecycle()
    val isOwnerUnlocked by viewModel.isOwnerUnlocked.collectAsStateWithLifecycle()
    val publishFeedbackMessage by viewModel.publishFeedbackMessage.collectAsStateWithLifecycle()

    val isOwner = viewModel.isUserOwner(rewardUser.email)
    var showEmailDialog by remember { mutableStateOf(false) }

    // Intercept back button if on child screens
    BackHandler(enabled = currentScreen !is ScreenDestination.Home) {
        viewModel.navigateBack()
    }

    // Celebration Dialog popup
    celebration?.let { cel ->
        CelebrationDialog(
            celebration = cel,
            onDismiss = { viewModel.dismissCelebration() },
            onOpenWallet = {
                viewModel.dismissCelebration()
                viewModel.navigateTo(ScreenDestination.RewardWallet)
            }
        )
    }

    // Publish feedback dialog
    publishFeedbackMessage?.let { msg ->
        AlertDialog(
            onDismissRequest = { viewModel.clearPublishFeedback() },
            title = {
                Text(
                    text = if (msg.startsWith("Published")) "Update Live on Feed! 🚀" else "Publication Notice",
                    style = MaterialTheme.typography.titleMedium
                )
            },
            text = { Text(text = msg) },
            confirmButton = {
                Button(onClick = { viewModel.clearPublishFeedback() }) {
                    Text("OK")
                }
            }
        )
    }

    // Email Switch Dialog popup
    if (showEmailDialog) {
        EmailSwitchDialog(
            currentEmail = rewardUser.email,
            onDismiss = { showEmailDialog = false },
            onSaveEmail = { newEmail ->
                viewModel.switchUserEmail(newEmail)
            }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (currentScreen is ScreenDestination.Home) {
                HindustanAppBar(
                    selectedCategory = selectedCategory,
                    onCategorySelected = { viewModel.selectCategory(it) },
                    searchQuery = searchQuery,
                    onSearchQueryChanged = { viewModel.updateSearchQuery(it) },
                    rewardUser = rewardUser,
                    isOwner = isOwner,
                    onOpenPublishStudio = { viewModel.navigateTo(ScreenDestination.PublishArticle) },
                    onOpenWallet = { viewModel.navigateTo(ScreenDestination.RewardWallet) },
                    onOpenBookmarks = { viewModel.navigateTo(ScreenDestination.Bookmarks) },
                    onOpenProfile = { showEmailDialog = true }
                )
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            when (val screen = currentScreen) {
                is ScreenDestination.Home -> {
                    val displayedArticles = remember(allArticles, selectedCategory, searchQuery) {
                        val q = searchQuery.trim().lowercase()
                        val base = if (selectedCategory == NewsCategory.HOME) {
                            allArticles
                        } else {
                            allArticles.filter { it.category == selectedCategory }
                        }
                        if (q.isEmpty()) {
                            base
                        } else {
                            base.filter {
                                it.title.lowercase().contains(q) ||
                                it.summary.lowercase().contains(q) ||
                                it.city?.lowercase()?.contains(q) == true ||
                                it.author.lowercase().contains(q)
                            }
                        }
                    }

                    HomeScreen(
                        selectedCategory = selectedCategory,
                        searchQuery = searchQuery,
                        articles = displayedArticles,
                        savedArticleIds = savedArticleIds,
                        rewardUser = rewardUser,
                        isOwner = isOwner,
                        onOpenPublishStudio = { viewModel.navigateTo(ScreenDestination.PublishArticle) },
                        onArticleClick = { articleId ->
                            viewModel.navigateTo(ScreenDestination.ArticleDetail(articleId))
                        },
                        onBookmarkClick = { articleId ->
                            viewModel.toggleBookmark(articleId)
                        },
                        onOpenWallet = {
                            viewModel.navigateTo(ScreenDestination.RewardWallet)
                        }
                    )
                }

                is ScreenDestination.ArticleDetail -> {
                    val article = allArticles.firstOrNull { it.id == screen.articleId }
                        ?: viewModel.newsRepository.getArticleById(screen.articleId)
                    if (article != null) {
                        val related = allArticles
                            .filter { it.category == article.category && it.id != article.id }
                            .ifEmpty {
                                allArticles.filter { it.id != article.id }.take(3)
                            }

                        ArticleDetailScreen(
                            article = article,
                            isBookmarked = savedArticleIds.contains(article.id),
                            timerState = timerState,
                            coinsEarnedToday = rewardUser.coinsEarnedToday,
                            maxDailyCoins = rewardUser.maxDailyCoins,
                            relatedArticles = related,
                            onBackClick = { viewModel.navigateBack() },
                            onBookmarkClick = { viewModel.toggleBookmark(article.id) },
                            onFastForwardTimer = { viewModel.fastForwardTimer(30) },
                            onRelatedArticleClick = { nextId ->
                                viewModel.navigateTo(ScreenDestination.ArticleDetail(nextId))
                            }
                        )
                    } else {
                        viewModel.navigateBack()
                    }
                }

                is ScreenDestination.RewardWallet -> {
                    RewardWalletScreen(
                        rewardUser = rewardUser,
                        transactions = transactions,
                        vouchers = viewModel.rewardRepository.availableVouchers,
                        isWithdrawing = isWithdrawing,
                        withdrawalMessage = upiWithdrawalMessage,
                        onBackClick = { viewModel.navigateBack() },
                        onWithdrawToUpi = { coins, upiId ->
                            viewModel.withdrawToUpi(coins, upiId)
                        },
                        onRedeemVoucher = { voucher ->
                            viewModel.redeemVoucher(voucher)
                        },
                        onAddDemoCoins = { viewModel.addDemoBonusCoins(10000) },
                        onChangeEmailClick = { showEmailDialog = true },
                        onClearWithdrawalMessage = { viewModel.clearWithdrawalMessage() }
                    )
                }

                is ScreenDestination.Bookmarks -> {
                    val bookmarkedArticles = allArticles.filter { savedArticleIds.contains(it.id) }

                    BookmarksScreen(
                        savedArticles = bookmarkedArticles,
                        onBackClick = { viewModel.navigateBack() },
                        onArticleClick = { articleId ->
                            viewModel.navigateTo(ScreenDestination.ArticleDetail(articleId))
                        },
                        onBookmarkClick = { articleId ->
                            viewModel.toggleBookmark(articleId)
                        }
                    )
                }

                is ScreenDestination.PublishArticle -> {
                    PublishArticleScreen(
                        currentEmail = rewardUser.email,
                        isOwner = isOwner,
                        onBackClick = { viewModel.navigateBack() },
                        onUnlockWithPasskey = { passkey ->
                            viewModel.unlockOwnerWithPasskey(passkey)
                        },
                        onPublish = { title, summary, content, bulletPoints, category, author, imageUrl, city, isBreaking ->
                            viewModel.publishArticle(
                                title = title,
                                summary = summary,
                                content = content,
                                bulletPoints = bulletPoints,
                                category = category,
                                author = author,
                                imageUrl = imageUrl,
                                city = city,
                                isBreaking = isBreaking
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}

