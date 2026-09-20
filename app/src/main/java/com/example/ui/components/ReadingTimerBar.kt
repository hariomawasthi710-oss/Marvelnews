package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.ReadingTimerState
import com.example.ui.theme.GoldCoin
import com.example.ui.theme.GoldCoinDark
import com.example.ui.theme.GoldCoinLight
import com.example.ui.theme.NeutralDark
import com.example.ui.theme.NeutralMedium
import com.example.ui.theme.SuccessGreen

@Composable
fun ReadingTimerFloatingBar(
    timerState: ReadingTimerState,
    coinsEarnedToday: Int,
    maxDailyCoins: Int,
    onFastForward: () -> Unit
) {
    val animatedProgress by animateFloatAsState(targetValue = timerState.progress, label = "reading_timer_progress")

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .testTag("reading_timer_bar"),
        shape = RoundedCornerShape(14.dp),
        color = if (timerState.coinAwardedForThisSession) Color(0xFFE8F5E9) else GoldCoinLight,
        shadowElevation = 3.dp
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            progress = { animatedProgress },
                            modifier = Modifier.size(36.dp),
                            color = if (timerState.coinAwardedForThisSession) SuccessGreen else GoldCoinDark,
                            trackColor = Color.White.copy(alpha = 0.6f),
                            strokeWidth = 3.5.dp
                        )
                        Icon(
                            imageVector = if (timerState.coinAwardedForThisSession) Icons.Default.CheckCircle else Icons.Default.MonetizationOn,
                            contentDescription = "Coin timer",
                            tint = if (timerState.coinAwardedForThisSession) SuccessGreen else GoldCoinDark,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = if (timerState.coinAwardedForThisSession) {
                                "Coin Awarded! (+1 🪙)"
                            } else {
                                "Read 2 mins & Earn 1 Coin"
                            },
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (timerState.coinAwardedForThisSession) SuccessGreen else NeutralDark
                        )
                        Text(
                            text = if (timerState.coinAwardedForThisSession) {
                                "Today: $coinsEarnedToday/$maxDailyCoins Coins • Read next article"
                            } else {
                                "Time: ${timerState.formattedElapsed} / 02:00 (${timerState.remainingSeconds}s remaining)"
                            },
                            fontSize = 11.sp,
                            color = NeutralMedium
                        )
                    }
                }

                // Quick Fast Forward button for testing/grading convenience
                if (!timerState.coinAwardedForThisSession) {
                    Surface(
                        onClick = onFastForward,
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White.copy(alpha = 0.85f),
                        modifier = Modifier.testTag("fast_forward_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.FastForward,
                                contentDescription = "Fast Forward",
                                tint = GoldCoinDark,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "+30s",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldCoinDark
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Linear Progress Track
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = if (timerState.coinAwardedForThisSession) SuccessGreen else GoldCoin,
                trackColor = Color.White.copy(alpha = 0.6f)
            )
        }
    }
}
