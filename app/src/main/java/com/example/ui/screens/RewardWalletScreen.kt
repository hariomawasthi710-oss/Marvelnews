package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CoinTransaction
import com.example.data.model.RewardUser
import com.example.data.model.ShoppingVoucher
import com.example.ui.theme.CrimsonLight
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.GoldCoin
import com.example.ui.theme.GoldCoinDark
import com.example.ui.theme.GoldCoinLight
import com.example.ui.theme.NeutralDark
import com.example.ui.theme.NeutralMedium
import com.example.ui.theme.SaffronOrange
import com.example.ui.theme.SuccessGreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardWalletScreen(
    rewardUser: RewardUser,
    transactions: List<CoinTransaction>,
    vouchers: List<ShoppingVoucher>,
    isWithdrawing: Boolean,
    withdrawalMessage: String?,
    onBackClick: () -> Unit,
    onWithdrawToUpi: (coins: Int, upiId: String) -> Unit,
    onRedeemVoucher: (ShoppingVoucher) -> Unit,
    onAddDemoCoins: () -> Unit,
    onChangeEmailClick: () -> Unit,
    onClearWithdrawalMessage: () -> Unit
) {
    var upiInput by remember { mutableStateOf("") }
    var selectedCoinsToWithdraw by remember { mutableIntStateOf(10000) }
    var inputError by remember { mutableStateOf<String?>(null) }

    val upiSuffixes = listOf("@okaxis", "@paytm", "@ybl", "@upi", "@okhdfcbank")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Rewards & Wallet",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick, modifier = Modifier.testTag("wallet_back_button")) {
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("reward_wallet_column"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Card 1: Balance and INR Cash Value
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(CrimsonRed, SaffronOrange)
                                )
                            )
                            .padding(20.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Total Coin Balance",
                                    color = Color.White.copy(alpha = 0.9f),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )

                                Surface(
                                    color = Color.White.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = "10,000 Coins = ₹5.00 INR",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.MonetizationOn,
                                    contentDescription = null,
                                    tint = GoldCoin,
                                    modifier = Modifier.size(36.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "${rewardUser.coinBalance}",
                                    color = Color.White,
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "≈ ₹${"%.2f".format(rewardUser.inrValue)} Cash",
                                    color = GoldCoin,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Lifetime Earned: ${rewardUser.lifetimeCoins} Coins",
                                    color = Color.White.copy(alpha = 0.85f),
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "Withdrawn: ₹${"%.2f".format(rewardUser.totalWithdrawnInr)}",
                                    color = Color.White.copy(alpha = 0.85f),
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }

            // Card 2: Daily Earning Limit Progress
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Today's Daily Quota",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = NeutralDark
                                )
                                Text(
                                    text = "Max 10 Coins per day per email",
                                    fontSize = 11.sp,
                                    color = NeutralMedium
                                )
                            }

                            Text(
                                text = "${rewardUser.coinsEarnedToday} / ${rewardUser.maxDailyCoins} Coins",
                                fontWeight = FontWeight.Black,
                                fontSize = 15.sp,
                                color = if (rewardUser.coinsEarnedToday >= 10) CrimsonRed else SuccessGreen
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        val progress = (rewardUser.coinsEarnedToday.toFloat() / rewardUser.maxDailyCoins.toFloat()).coerceIn(0f, 1f)
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = if (rewardUser.coinsEarnedToday >= 10) CrimsonRed else SaffronOrange,
                            trackColor = Color(0xFFEEEEEE)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Email info row with switch action
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = null,
                                    tint = NeutralMedium,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = rewardUser.email,
                                    fontSize = 11.sp,
                                    color = NeutralMedium,
                                    maxLines = 1
                                )
                            }

                            TextButton(
                                onClick = onChangeEmailClick,
                                modifier = Modifier.testTag("change_email_button")
                            ) {
                                Text("Change Email", fontSize = 11.sp, color = CrimsonRed, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Card 3: Quick Demo / Tester Booster Button
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = GoldCoinLight),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AddCircle,
                                contentDescription = null,
                                tint = GoldCoinDark,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "⚡ Demo Booster: Add +10,000 Coins",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NeutralDark
                                )
                                Text(
                                    text = "Test instant UPI withdrawal (+₹5.00 INR)",
                                    fontSize = 11.sp,
                                    color = NeutralMedium
                                )
                            }
                        }

                        Button(
                            onClick = onAddDemoCoins,
                            colors = ButtonDefaults.buttonColors(containerColor = GoldCoinDark),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("add_test_coins_button")
                        ) {
                            Text("+10K", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Message Banner (Success or Error for Withdrawal)
            if (withdrawalMessage != null) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (withdrawalMessage.startsWith("Success") || withdrawalMessage.startsWith("Voucher")) {
                                Color(0xFFE8F5E9)
                            } else {
                                CrimsonLight
                            }
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (withdrawalMessage.startsWith("Success") || withdrawalMessage.startsWith("Voucher")) {
                                    Icons.Default.CheckCircle
                                } else {
                                    Icons.Default.Warning
                                },
                                contentDescription = null,
                                tint = if (withdrawalMessage.startsWith("Success") || withdrawalMessage.startsWith("Voucher")) {
                                    SuccessGreen
                                } else {
                                    CrimsonRed
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = withdrawalMessage,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = NeutralDark,
                                modifier = Modifier.weight(1f)
                            )
                            TextButton(onClick = onClearWithdrawalMessage) {
                                Text("Dismiss", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            // Card 4: UPI Withdrawal Section
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.QrCode,
                                contentDescription = null,
                                tint = CrimsonRed,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Withdraw via UPI",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Minimum threshold: 10,000 Coins = ₹5.00 cash directly to bank.",
                            fontSize = 11.sp,
                            color = NeutralMedium
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Amount Selection Chips
                        Text(
                            text = "Select withdrawal amount:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(
                                10000 to "₹5.00",
                                20000 to "₹10.00",
                                50000 to "₹25.00"
                            ).forEach { (coins, inr) ->
                                FilterChip(
                                    selected = selectedCoinsToWithdraw == coins,
                                    onClick = { selectedCoinsToWithdraw = coins },
                                    label = { Text("$coins Coins ($inr)", fontSize = 11.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = CrimsonRed,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // UPI ID Input
                        OutlinedTextField(
                            value = upiInput,
                            onValueChange = {
                                upiInput = it
                                inputError = null
                            },
                            label = { Text("Enter your UPI ID") },
                            placeholder = { Text("e.g. 9876543210@paytm or user@okaxis") },
                            singleLine = true,
                            isError = inputError != null,
                            supportingText = inputError?.let { { Text(it, color = CrimsonRed) } },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("upi_id_input")
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Quick Suffix Helpers
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            upiSuffixes.take(4).forEach { suffix ->
                                Surface(
                                    onClick = {
                                        val base = upiInput.substringBefore("@")
                                        upiInput = if (base.isNotEmpty()) "$base$suffix" else "user$suffix"
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFFF0F0F0)
                                ) {
                                    Text(
                                        text = suffix,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Withdraw Action Button
                        Button(
                            onClick = {
                                if (rewardUser.coinBalance < selectedCoinsToWithdraw) {
                                    inputError = "Insufficient coins! You have ${rewardUser.coinBalance} coins. At least $selectedCoinsToWithdraw required."
                                } else if (!upiInput.contains("@") || upiInput.length < 5) {
                                    inputError = "Please enter a valid UPI ID (e.g. user@okaxis)"
                                } else {
                                    inputError = null
                                    onWithdrawToUpi(selectedCoinsToWithdraw, upiInput)
                                }
                            },
                            enabled = !isWithdrawing,
                            colors = ButtonDefaults.buttonColors(containerColor = CrimsonRed),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("withdraw_button")
                        ) {
                            if (isWithdrawing) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Processing...")
                            } else {
                                Text(
                                    text = "Withdraw ₹${"%.2f".format((selectedCoinsToWithdraw / 10000.0) * 5.0)} to UPI",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }

            // Card 5: Shopping Vouchers
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.ShoppingBag,
                                contentDescription = null,
                                tint = SaffronOrange,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Shopping Vouchers & Coupons",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Redeem shopping & recharge vouchers for 10,000 coins:",
                            fontSize = 11.sp,
                            color = NeutralMedium
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        vouchers.forEach { voucher ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA)),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = voucher.title,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                        Text(
                                            text = "${voucher.brandName} • 10,000 Coins",
                                            fontSize = 11.sp,
                                            color = NeutralMedium
                                        )
                                    }

                                    Button(
                                        onClick = { onRedeemVoucher(voucher) },
                                        enabled = rewardUser.coinBalance >= voucher.coinCost,
                                        colors = ButtonDefaults.buttonColors(containerColor = SaffronOrange),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.testTag("redeem_${voucher.id}")
                                    ) {
                                        Text("Redeem", fontSize = 11.sp, color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Card 6: Transaction History
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null,
                        tint = NeutralMedium,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Transaction History",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            if (transactions.isEmpty()) {
                item {
                    Text(
                        text = "No transactions yet. Read news for 2 minutes to earn coins!",
                        color = NeutralMedium,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                }
            } else {
                items(transactions, key = { it.id }) { txn ->
                    val isCredit = txn.coins > 0
                    val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
                    val dateStr = sdf.format(Date(txn.timestamp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = txn.note,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "$dateStr • ${txn.status}",
                                    fontSize = 11.sp,
                                    color = NeutralMedium
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = if (isCredit) "+${txn.coins} 🪙" else "${txn.coins} 🪙",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = if (isCredit) SuccessGreen else CrimsonRed
                                )
                                Text(
                                    text = if (isCredit) "+₹${"%.4f".format(txn.inrAmount)}" else "-₹${"%.2f".format(kotlin.math.abs(txn.inrAmount))}",
                                    fontSize = 11.sp,
                                    color = NeutralMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
