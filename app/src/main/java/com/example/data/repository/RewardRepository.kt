package com.example.data.repository

import com.example.data.db.CoinTransactionEntity
import com.example.data.db.DailyEarningEntity
import com.example.data.db.RewardDao
import com.example.data.db.UserProfileEntity
import com.example.data.model.CoinTransaction
import com.example.data.model.RewardUser
import com.example.data.model.ShoppingVoucher
import com.example.data.model.TransactionType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RewardRepository(
    private val rewardDao: RewardDao,
    private val externalScope: CoroutineScope
) {
    private val _currentEmail = MutableStateFlow("hariomawasthi710@gmail.com")
    val currentEmail: Flow<String> = _currentEmail.asStateFlow()

    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    private fun getTodayDateString(): String = dateFormatter.format(Date())

    // Vouchers catalog for 10,000 coins (= ₹5 INR value)
    val availableVouchers = listOf(
        ShoppingVoucher(
            id = "v-amazon-5",
            brandName = "Amazon Pay",
            title = "₹5 Amazon Pay Gift Voucher",
            coinCost = 10000,
            inrValue = 5.0,
            category = "Shopping",
            promoCode = "AMZ5-MARVEL-GIFT"
        ),
        ShoppingVoucher(
            id = "v-flipkart-5",
            brandName = "Flipkart",
            title = "₹5 Flipkart Shopping Coupon",
            coinCost = 10000,
            inrValue = 5.0,
            category = "Shopping",
            promoCode = "FK5-MARVEL-COIN"
        ),
        ShoppingVoucher(
            id = "v-recharge-5",
            brandName = "Mobile Recharge",
            title = "₹5 Mobile Recharge Cashback",
            coinCost = 10000,
            inrValue = 5.0,
            category = "Recharge",
            promoCode = "REC5-TALKTIME-2026"
        )
    )

    init {
        externalScope.launch {
            ensureUserProfileInitialized(_currentEmail.value)
        }
    }

    fun switchEmail(newEmail: String) {
        val trimmed = newEmail.trim().lowercase()
        if (trimmed.isNotEmpty() && trimmed != _currentEmail.value) {
            _currentEmail.value = trimmed
            externalScope.launch {
                ensureUserProfileInitialized(trimmed)
            }
        }
    }

    private suspend fun ensureUserProfileInitialized(email: String) {
        val existing = rewardDao.getUserProfile(email)
        if (existing == null) {
            rewardDao.saveUserProfile(
                UserProfileEntity(
                    email = email,
                    coinsBalance = 0,
                    lifetimeCoins = 0,
                    totalWithdrawnInr = 0.0
                )
            )
        }
        val today = getTodayDateString()
        val daily = rewardDao.getDailyEarning(email, today)
        if (daily == null) {
            rewardDao.saveDailyEarning(
                DailyEarningEntity(
                    email = email,
                    dateString = today,
                    coinsEarnedToday = 0
                )
            )
        }
    }

    fun observeUserReward(email: String): Flow<RewardUser> {
        val today = getTodayDateString()
        val profileFlow = rewardDao.observeUserProfile(email)
        val dailyFlow = rewardDao.observeDailyEarning(email, today)

        return combine(profileFlow, dailyFlow) { profile, daily ->
            RewardUser(
                email = email,
                coinBalance = profile?.coinsBalance ?: 0,
                coinsEarnedToday = daily?.coinsEarnedToday ?: 0,
                maxDailyCoins = 10,
                lifetimeCoins = profile?.lifetimeCoins ?: 0,
                totalWithdrawnInr = profile?.totalWithdrawnInr ?: 0.0
            )
        }
    }

    fun observeTransactions(email: String): Flow<List<CoinTransaction>> {
        return rewardDao.observeTransactions(email).combine(MutableStateFlow(Unit)) { entities, _ ->
            entities.map { entity ->
                CoinTransaction(
                    id = entity.id,
                    email = entity.email,
                    type = when (entity.type) {
                        "READING_REWARD" -> TransactionType.READING_REWARD
                        "UPI_WITHDRAWAL" -> TransactionType.UPI_WITHDRAWAL
                        "SHOPPING_VOUCHER" -> TransactionType.SHOPPING_VOUCHER
                        else -> TransactionType.BONUS_TEST
                    },
                    coins = entity.coins,
                    inrAmount = entity.inrAmount,
                    note = entity.note,
                    timestamp = entity.timestamp,
                    status = entity.status,
                    referenceDetails = entity.referenceDetails
                )
            }
        }
    }

    /**
     * Award 1 coin for completing 2 minutes of active reading on an article.
     * Enforces the 10 coins/day per email limit.
     */
    suspend fun awardReadingReward(articleTitle: String): Pair<Boolean, String> = withContext(Dispatchers.IO) {
        val email = _currentEmail.value
        val today = getTodayDateString()

        ensureUserProfileInitialized(email)

        val daily = rewardDao.getDailyEarning(email, today) ?: DailyEarningEntity(
            email = email,
            dateString = today,
            coinsEarnedToday = 0
        )

        if (daily.coinsEarnedToday >= 10) {
            return@withContext Pair(
                false,
                "Daily limit reached! You can earn up to 10 coins per day per email. Come back tomorrow!"
            )
        }

        val updatedDailyEarned = daily.coinsEarnedToday + 1
        rewardDao.saveDailyEarning(
            daily.copy(
                coinsEarnedToday = updatedDailyEarned,
                lastUpdated = System.currentTimeMillis()
            )
        )

        val profile = rewardDao.getUserProfile(email) ?: UserProfileEntity(email = email)
        val newBalance = profile.coinsBalance + 1
        val newLifetime = profile.lifetimeCoins + 1
        rewardDao.saveUserProfile(
            profile.copy(
                coinsBalance = newBalance,
                lifetimeCoins = newLifetime
            )
        )

        rewardDao.insertTransaction(
            CoinTransactionEntity(
                email = email,
                type = "READING_REWARD",
                coins = 1,
                inrAmount = 0.0005, // 1 coin = ₹0.0005 (10,000 coins = ₹5)
                note = "2-Min Reading Reward: ${articleTitle.take(35)}...",
                status = "SUCCESS",
                referenceDetails = "2-Minute Reading Reward"
            )
        )

        return@withContext Pair(
            true,
            "Congratulations! You earned 1 Coin! You have earned $updatedDailyEarned/10 coins today."
        )
    }

    /**
     * Withdraw coins to UPI.
     * Minimum 10,000 coins = ₹5.00 INR.
     */
    suspend fun withdrawCoinsViaUpi(
        coinsToWithdraw: Int,
        upiId: String
    ): Result<String> = withContext(Dispatchers.IO) {
        val email = _currentEmail.value
        if (coinsToWithdraw < 10000) {
            return@withContext Result.failure(Exception("Minimum withdrawal threshold is 10,000 coins (₹5.00)."))
        }

        val cleanedUpi = upiId.trim()
        if (!cleanedUpi.contains("@") || cleanedUpi.length < 5) {
            return@withContext Result.failure(Exception("Please enter a valid UPI ID (e.g. username@upi or 9876543210@paytm)."))
        }

        val profile = rewardDao.getUserProfile(email)
            ?: return@withContext Result.failure(Exception("User profile not found."))

        if (profile.coinsBalance < coinsToWithdraw) {
            return@withContext Result.failure(Exception("Insufficient balance! You have ${profile.coinsBalance} coins, but $coinsToWithdraw are required."))
        }

        val inrAmount = (coinsToWithdraw / 10000.0) * 5.0
        val updatedBalance = profile.coinsBalance - coinsToWithdraw
        val updatedWithdrawnInr = profile.totalWithdrawnInr + inrAmount

        rewardDao.saveUserProfile(
            profile.copy(
                coinsBalance = updatedBalance,
                totalWithdrawnInr = updatedWithdrawnInr
            )
        )

        val txnId = "UPI-TXN-${System.currentTimeMillis() % 1000000}"
        rewardDao.insertTransaction(
            CoinTransactionEntity(
                email = email,
                type = "UPI_WITHDRAWAL",
                coins = -coinsToWithdraw,
                inrAmount = -inrAmount,
                note = "UPI Withdrawal (₹${"%.2f".format(inrAmount)}) -> $cleanedUpi",
                status = "SUCCESS",
                referenceDetails = "Ref: $txnId | UPI: $cleanedUpi"
            )
        )

        return@withContext Result.success("Success! ₹${"%.2f".format(inrAmount)} has been credited to your UPI ID ($cleanedUpi). Reference: $txnId")
    }

    /**
     * Redeem shopping voucher with 10,000 coins.
     */
    suspend fun redeemShoppingVoucher(voucher: ShoppingVoucher): Result<String> = withContext(Dispatchers.IO) {
        val email = _currentEmail.value
        val profile = rewardDao.getUserProfile(email)
            ?: return@withContext Result.failure(Exception("Profile not found."))

        if (profile.coinsBalance < voucher.coinCost) {
            return@withContext Result.failure(Exception("Insufficient balance! 10,000 coins required for this voucher."))
        }

        val updatedBalance = profile.coinsBalance - voucher.coinCost
        rewardDao.saveUserProfile(
            profile.copy(coinsBalance = updatedBalance)
        )

        val uniqueCode = "${voucher.promoCode}-${(1000..9999).random()}"
        rewardDao.insertTransaction(
            CoinTransactionEntity(
                email = email,
                type = "SHOPPING_VOUCHER",
                coins = -voucher.coinCost,
                inrAmount = -voucher.inrValue,
                note = "Voucher: ${voucher.title} (Code: $uniqueCode)",
                status = "SUCCESS",
                referenceDetails = uniqueCode
            )
        )

        return@withContext Result.success("Voucher Redeemed! Your voucher code is: $uniqueCode")
    }

    /**
     * Demo Tester function to instantly add 10,000 coins (₹5)
     */
    suspend fun addTestDemoCoins(amount: Int = 10000) = withContext(Dispatchers.IO) {
        val email = _currentEmail.value
        ensureUserProfileInitialized(email)
        val profile = rewardDao.getUserProfile(email) ?: UserProfileEntity(email = email)
        val inrValue = (amount / 10000.0) * 5.0
        rewardDao.saveUserProfile(
            profile.copy(
                coinsBalance = profile.coinsBalance + amount,
                lifetimeCoins = profile.lifetimeCoins + amount
            )
        )
        rewardDao.insertTransaction(
            CoinTransactionEntity(
                email = email,
                type = "BONUS_TEST",
                coins = amount,
                inrAmount = inrValue,
                note = "Test Booster ($amount Coins = ₹${"%.2f".format(inrValue)})",
                status = "SUCCESS",
                referenceDetails = "Instant Test Credit"
            )
        )
    }
}
