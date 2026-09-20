package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.model.CoinTransaction
import com.example.data.model.NewsArticle
import com.example.data.model.NewsCategory
import com.example.data.model.RewardUser
import com.example.data.model.ShoppingVoucher
import com.example.data.repository.NewsRepository
import com.example.data.repository.RewardRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class ScreenDestination {
    object Home : ScreenDestination()
    data class ArticleDetail(val articleId: String) : ScreenDestination()
    object RewardWallet : ScreenDestination()
    object Bookmarks : ScreenDestination()
    object PublishArticle : ScreenDestination()
}

data class ReadingTimerState(
    val articleId: String = "",
    val secondsElapsed: Int = 0,
    val targetSeconds: Int = 120, // 2 minutes
    val isRunning: Boolean = false,
    val coinAwardedForThisSession: Boolean = false
) {
    val progress: Float
        get() = (secondsElapsed.toFloat() / targetSeconds.toFloat()).coerceIn(0f, 1f)

    val remainingSeconds: Int
        get() = (targetSeconds - secondsElapsed).coerceAtLeast(0)

    val formattedElapsed: String
        get() = "%02d:%02d".format(secondsElapsed / 60, secondsElapsed % 60)

    val formattedRemaining: String
        get() = "%02d:%02d".format(remainingSeconds / 60, remainingSeconds % 60)
}

data class RewardCelebration(
    val coinsAwarded: Int,
    val message: String,
    val isSuccess: Boolean
)

class NewsViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val OWNER_EMAIL = "hariomawasthi710@gmail.com"
        const val OWNER_PASSKEY = "7100"
    }

    private val db = AppDatabase.getDatabase(application)
    val newsRepository = NewsRepository(db.savedArticleDao(), db.publishedArticleDao())
    val rewardRepository = RewardRepository(db.rewardDao(), viewModelScope)

    // Current navigation destination
    private val _currentScreen = MutableStateFlow<ScreenDestination>(ScreenDestination.Home)
    val currentScreen: StateFlow<ScreenDestination> = _currentScreen.asStateFlow()

    // Selected Category
    private val _selectedCategory = MutableStateFlow(NewsCategory.HOME)
    val selectedCategory: StateFlow<NewsCategory> = _selectedCategory.asStateFlow()

    // Search query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Owner override state
    private val _isOwnerUnlocked = MutableStateFlow(false)
    val isOwnerUnlocked: StateFlow<Boolean> = _isOwnerUnlocked.asStateFlow()

    // Live articles stream (including published updates from owner)
    val allArticles: StateFlow<List<NewsArticle>> = newsRepository.observeAllArticles()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), newsRepository.getAllArticles())

    // Saved Bookmarks
    val savedArticleIds: StateFlow<List<String>> = newsRepository.getSavedArticleIds()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active reading timer
    private val _timerState = MutableStateFlow(ReadingTimerState())
    val timerState: StateFlow<ReadingTimerState> = _timerState.asStateFlow()

    private var timerJob: Job? = null

    // Reward celebration dialog state
    private val _celebration = MutableStateFlow<RewardCelebration?>(null)
    val celebration: StateFlow<RewardCelebration?> = _celebration.asStateFlow()

    // User reward profile observed reactively for current email
    val rewardUser: StateFlow<RewardUser> = rewardRepository.currentEmail
        .flatMapLatest { email -> rewardRepository.observeUserReward(email) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            RewardUser(
                email = OWNER_EMAIL,
                coinBalance = 0,
                coinsEarnedToday = 0,
                maxDailyCoins = 10,
                lifetimeCoins = 0,
                totalWithdrawnInr = 0.0
            )
        )

    val transactions: StateFlow<List<CoinTransaction>> = rewardRepository.currentEmail
        .flatMapLatest { email -> rewardRepository.observeTransactions(email) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // UPI withdrawal feedback state
    private val _upiWithdrawalMessage = MutableStateFlow<String?>(null)
    val upiWithdrawalMessage: StateFlow<String?> = _upiWithdrawalMessage.asStateFlow()

    private val _isWithdrawing = MutableStateFlow(false)
    val isWithdrawing: StateFlow<Boolean> = _isWithdrawing.asStateFlow()

    // Publishing feedback
    private val _publishFeedbackMessage = MutableStateFlow<String?>(null)
    val publishFeedbackMessage: StateFlow<String?> = _publishFeedbackMessage.asStateFlow()

    fun isUserOwner(email: String = rewardUser.value.email): Boolean {
        return email.equals(OWNER_EMAIL, ignoreCase = true) || _isOwnerUnlocked.value
    }

    fun unlockOwnerWithPasskey(passkey: String): Boolean {
        if (passkey.trim() == OWNER_PASSKEY) {
            _isOwnerUnlocked.value = true
            return true
        }
        return false
    }

    fun selectCategory(category: NewsCategory) {
        _selectedCategory.value = category
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun navigateTo(screen: ScreenDestination) {
        _currentScreen.value = screen
        if (screen is ScreenDestination.ArticleDetail) {
            startReadingTimer(screen.articleId)
        } else {
            stopReadingTimer()
        }
    }

    fun navigateBack(): Boolean {
        return if (_currentScreen.value !is ScreenDestination.Home) {
            stopReadingTimer()
            _currentScreen.value = ScreenDestination.Home
            true
        } else {
            false
        }
    }

    fun toggleBookmark(articleId: String) {
        viewModelScope.launch {
            newsRepository.toggleBookmark(articleId)
        }
    }

    /**
     * Start the 2-minute active reading timer for an article.
     * Ticks every second until 120s is reached.
     */
    private fun startReadingTimer(articleId: String) {
        timerJob?.cancel()
        _timerState.value = ReadingTimerState(
            articleId = articleId,
            secondsElapsed = 0,
            targetSeconds = 120,
            isRunning = true,
            coinAwardedForThisSession = false
        )

        timerJob = viewModelScope.launch {
            while (_timerState.value.isRunning && _timerState.value.secondsElapsed < _timerState.value.targetSeconds) {
                delay(1000L)
                val newElapsed = _timerState.value.secondsElapsed + 1
                _timerState.value = _timerState.value.copy(secondsElapsed = newElapsed)

                if (newElapsed >= _timerState.value.targetSeconds && !_timerState.value.coinAwardedForThisSession) {
                    _timerState.value = _timerState.value.copy(
                        coinAwardedForThisSession = true,
                        isRunning = false
                    )
                    // Trigger coin award
                    val article = allArticles.value.firstOrNull { it.id == articleId }
                        ?: newsRepository.getArticleById(articleId)
                    val articleTitle = article?.title ?: "Article"
                    val (success, message) = rewardRepository.awardReadingReward(articleTitle)
                    _celebration.value = RewardCelebration(
                        coinsAwarded = if (success) 1 else 0,
                        message = message,
                        isSuccess = success
                    )
                }
            }
        }
    }

    /**
     * For testing/demo convenience: jump timer forward by 30 seconds
     */
    fun fastForwardTimer(seconds: Int = 30) {
        if (!_timerState.value.coinAwardedForThisSession) {
            val current = _timerState.value.secondsElapsed
            val target = _timerState.value.targetSeconds
            val nextVal = (current + seconds).coerceAtMost(target)
            _timerState.value = _timerState.value.copy(secondsElapsed = nextVal)
            if (nextVal >= target) {
                viewModelScope.launch {
                    _timerState.value = _timerState.value.copy(
                        coinAwardedForThisSession = true,
                        isRunning = false
                    )
                    val article = allArticles.value.firstOrNull { it.id == _timerState.value.articleId }
                        ?: newsRepository.getArticleById(_timerState.value.articleId)
                    val articleTitle = article?.title ?: "Article"
                    val (success, message) = rewardRepository.awardReadingReward(articleTitle)
                    _celebration.value = RewardCelebration(
                        coinsAwarded = if (success) 1 else 0,
                        message = message,
                        isSuccess = success
                    )
                }
            }
        }
    }

    fun dismissCelebration() {
        _celebration.value = null
    }

    private fun stopReadingTimer() {
        timerJob?.cancel()
        _timerState.value = _timerState.value.copy(isRunning = false)
    }

    fun publishArticle(
        title: String,
        summary: String,
        content: List<String>,
        bulletPoints: List<String>,
        category: NewsCategory,
        author: String,
        imageUrl: String,
        city: String?,
        isBreaking: Boolean
    ) {
        viewModelScope.launch {
            val currentEmail = rewardUser.value.email
            if (!isUserOwner(currentEmail)) {
                _publishFeedbackMessage.value = "Unauthorized: Only the owner ($OWNER_EMAIL) can publish updates to the public feed."
                return@launch
            }
            val result = newsRepository.publishArticle(
                title = title,
                summary = summary,
                content = content,
                bulletPoints = bulletPoints,
                category = category,
                author = author,
                imageUrl = imageUrl,
                city = city,
                isBreaking = isBreaking,
                publisherEmail = currentEmail
            )
            if (result.isSuccess) {
                _publishFeedbackMessage.value = "Published successfully! Your update is now live on the public feed."
                navigateTo(ScreenDestination.Home)
            } else {
                _publishFeedbackMessage.value = "Error: ${result.exceptionOrNull()?.message}"
            }
        }
    }

    fun clearPublishFeedback() {
        _publishFeedbackMessage.value = null
    }

    fun withdrawToUpi(coins: Int, upiId: String) {
        viewModelScope.launch {
            _isWithdrawing.value = true
            _upiWithdrawalMessage.value = null
            delay(600)
            val result = rewardRepository.withdrawCoinsViaUpi(coins, upiId)
            _isWithdrawing.value = false
            if (result.isSuccess) {
                _upiWithdrawalMessage.value = result.getOrNull()
            } else {
                _upiWithdrawalMessage.value = "Error: ${result.exceptionOrNull()?.message}"
            }
        }
    }

    fun clearWithdrawalMessage() {
        _upiWithdrawalMessage.value = null
    }

    fun redeemVoucher(voucher: ShoppingVoucher) {
        viewModelScope.launch {
            _isWithdrawing.value = true
            delay(500)
            val result = rewardRepository.redeemShoppingVoucher(voucher)
            _isWithdrawing.value = false
            _upiWithdrawalMessage.value = if (result.isSuccess) {
                result.getOrNull()
            } else {
                "Error: ${result.exceptionOrNull()?.message}"
            }
        }
    }

    fun addDemoBonusCoins(amount: Int = 10000) {
        viewModelScope.launch {
            rewardRepository.addTestDemoCoins(amount)
        }
    }

    fun switchUserEmail(email: String) {
        rewardRepository.switchEmail(email)
    }
}
