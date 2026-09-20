package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.db.AppDatabase
import com.example.data.model.NewsCategory
import com.example.data.repository.NewsRepository
import com.example.data.repository.RewardRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    private lateinit var database: AppDatabase
    private lateinit var newsRepository: NewsRepository
    private lateinit var rewardRepository: RewardRepository
    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        newsRepository = NewsRepository(database.savedArticleDao(), database.publishedArticleDao())
        rewardRepository = RewardRepository(database.rewardDao(), testScope)
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun `read app name from context is Marvel News`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Marvel News", appName)
    }

    @Test
    fun `all required categories are available in English`() {
        val categories = NewsCategory.entries.map { it.titleEnglish }
        assertTrue(categories.contains("Home"))
        assertTrue(categories.contains("News"))
        assertTrue(categories.contains("Finance"))
        assertTrue(categories.contains("Tech"))
        assertTrue(categories.contains("Health"))
        assertTrue(categories.contains("Digital Marketing"))
        assertTrue(categories.contains("Trending"))
    }

    @Test
    fun `only owner can publish latest updates to public feed and users can read it`() = runTest {
        val publishResult = newsRepository.publishArticle(
            title = "Exclusive: Next Marvel Blockbuster Teaser Revealed",
            summary = "Marvel Studios releases surprise first-look footage.",
            content = listOf(
                "Marvel Studios dropped an unexpected teaser during special presentations.",
                "Fans and critics are already analyzing the easter eggs and multiverse clues."
            ),
            bulletPoints = listOf("Exclusive teaser release", "Confirmed for next summer"),
            category = NewsCategory.TRENDING,
            author = "Hariom Awasthi (Owner)",
            imageUrl = "https://images.unsplash.com/photo-1607604276583-eef5d076aa5f",
            city = "New Delhi",
            isBreaking = true,
            publisherEmail = "hariomawasthi710@gmail.com"
        )

        assertTrue(publishResult.isSuccess)
        val publishedArticle = publishResult.getOrNull()
        assertNotNull(publishedArticle)
        assertTrue(publishedArticle!!.isOwnerPost)
        assertEquals("Exclusive: Next Marvel Blockbuster Teaser Revealed", publishedArticle.title)

        // Verify that public stream receives the published article at top of the feed
        val allArticles = newsRepository.observeAllArticles().first()
        val found = allArticles.find { it.id == publishedArticle.id }
        assertNotNull(found)
        assertEquals("Exclusive: Next Marvel Blockbuster Teaser Revealed", found?.title)
        assertTrue(found?.isOwnerPost == true)
    }

    @Test
    fun `earn 1 coin per 2 minutes reading and enforce 10 coins daily limit`() = runTest {
        rewardRepository.switchEmail("testuser@gmail.com")

        // Award 10 coins (1 coin at a time)
        for (i in 1..10) {
            val (success, _) = rewardRepository.awardReadingReward("Article #$i")
            assertTrue("Coin #$i should be awarded", success)
        }

        val user = rewardRepository.observeUserReward("testuser@gmail.com").first()
        assertEquals(10, user.coinBalance)
        assertEquals(10, user.coinsEarnedToday)

        // 11th coin should fail due to 10 coins/day limit
        val (eleventhSuccess, eleventhMsg) = rewardRepository.awardReadingReward("Article #11")
        assertFalse("11th coin must be blocked by daily limit", eleventhSuccess)
        assertTrue(eleventhMsg.contains("Daily limit reached"))
    }

    @Test
    fun `10000 coins equal 5 rupees valuation and allows UPI withdrawal`() = runTest {
        rewardRepository.switchEmail("withdrawuser@gmail.com")
        rewardRepository.addTestDemoCoins(10000)

        val userBefore = rewardRepository.observeUserReward("withdrawuser@gmail.com").first()
        assertEquals(10000, userBefore.coinBalance)
        assertEquals(5.0, userBefore.inrValue, 0.001)

        // Test UPI withdrawal with valid UPI ID
        val withdrawalResult = rewardRepository.withdrawCoinsViaUpi(10000, "withdrawuser@okaxis")
        assertTrue(withdrawalResult.isSuccess)

        val userAfter = rewardRepository.observeUserReward("withdrawuser@gmail.com").first()
        assertEquals(0, userAfter.coinBalance)
        assertEquals(5.0, userAfter.totalWithdrawnInr, 0.001)
    }

    @Test
    fun `UPI withdrawal requires minimum 10000 coins`() = runTest {
        rewardRepository.switchEmail("lowbalance@gmail.com")
        rewardRepository.addTestDemoCoins(5000)

        val withdrawalResult = rewardRepository.withdrawCoinsViaUpi(5000, "user@upi")
        assertFalse(withdrawalResult.isSuccess)
        assertTrue(withdrawalResult.exceptionOrNull()?.message?.contains("10,000") == true)
    }
}
