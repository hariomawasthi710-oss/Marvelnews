package com.example.data.model

data class RewardUser(
    val email: String,
    val coinBalance: Int,
    val coinsEarnedToday: Int,
    val maxDailyCoins: Int = 10,
    val lifetimeCoins: Int,
    val totalWithdrawnInr: Double
) {
    val inrValue: Double
        get() = (coinBalance / 10000.0) * 5.0

    val canEarnMoreToday: Boolean
        get() = coinsEarnedToday < maxDailyCoins

    val remainingCoinsToday: Int
        get() = (maxDailyCoins - coinsEarnedToday).coerceAtLeast(0)

    val canWithdraw: Boolean
        get() = coinBalance >= 10000
}

enum class TransactionType {
    READING_REWARD,
    UPI_WITHDRAWAL,
    SHOPPING_VOUCHER,
    BONUS_TEST
}

data class CoinTransaction(
    val id: Long = 0,
    val email: String,
    val type: TransactionType,
    val coins: Int,
    val inrAmount: Double,
    val note: String,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "SUCCESS", // SUCCESS, PROCESSING, PENDING
    val referenceDetails: String = "" // UPI ID or Coupon Code
)

data class ShoppingVoucher(
    val id: String,
    val brandName: String,
    val title: String,
    val coinCost: Int = 10000,
    val inrValue: Double = 5.0,
    val category: String,
    val promoCode: String,
    val expiryDays: Int = 30
)
