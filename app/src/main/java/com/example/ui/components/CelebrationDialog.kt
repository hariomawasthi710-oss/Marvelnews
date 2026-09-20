package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.RewardCelebration
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.GoldCoin
import com.example.ui.theme.GoldCoinDark
import com.example.ui.theme.GoldCoinLight
import com.example.ui.theme.NeutralDark
import com.example.ui.theme.NeutralMedium
import com.example.ui.theme.SaffronOrange

@Composable
fun CelebrationDialog(
    celebration: RewardCelebration,
    onDismiss: () -> Unit,
    onOpenWallet: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("celebration_dialog")
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Big glowing coin icon badge
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = if (celebration.isSuccess) {
                                    listOf(GoldCoin, GoldCoinDark)
                                } else {
                                    listOf(SaffronOrange, CrimsonRed)
                                }
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (celebration.isSuccess) Icons.Default.MonetizationOn else Icons.Default.Info,
                        contentDescription = "Reward Icon",
                        tint = Color.White,
                        modifier = Modifier.size(44.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (celebration.isSuccess) "Awesome! You Earned 1 Coin 🎉" else "Daily Limit Notice",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (celebration.isSuccess) GoldCoinDark else NeutralDark,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = celebration.message,
                    fontSize = 14.sp,
                    color = NeutralMedium,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                if (celebration.isSuccess) {
                    Surface(
                        color = GoldCoinLight,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "💡 Remember: 10,000 Coins = ₹5.00 INR Cash",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = NeutralDark
                            )
                            Text(
                                text = "Maximum 10 coins can be earned per day per email.",
                                fontSize = 11.sp,
                                color = NeutralMedium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Keep Reading")
                    }

                    Button(
                        onClick = {
                            onDismiss()
                            onOpenWallet()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SaffronOrange),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("View Wallet", color = Color.White)
                    }
                }
            }
        }
    }
}
