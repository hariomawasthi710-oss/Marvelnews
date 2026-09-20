package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.GoldCoinLight
import com.example.ui.theme.NeutralDark
import com.example.ui.theme.NeutralMedium

@Composable
fun EmailSwitchDialog(
    currentEmail: String,
    onDismiss: () -> Unit,
    onSaveEmail: (String) -> Unit
) {
    var emailInput by remember { mutableStateOf(currentEmail) }
    var errorText by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("email_switch_dialog")
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "User Account & Email",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeutralDark
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Rule: A maximum of 10 coins can be earned per email ID per day.",
                    fontSize = 12.sp,
                    color = NeutralMedium,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = emailInput,
                    onValueChange = {
                        emailInput = it
                        errorText = null
                    },
                    label = { Text("Enter Email Address") },
                    singleLine = true,
                    leadingIcon = {
                        Icon(Icons.Default.Email, contentDescription = null, tint = CrimsonRed)
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                if (errorText != null) {
                    Text(
                        text = errorText!!,
                        color = CrimsonRed,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                    color = GoldCoinLight,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "🔒 Each email account maintains an independent coin balance and daily 10-coin quota.",
                        fontSize = 11.sp,
                        color = NeutralDark,
                        modifier = Modifier.padding(8.dp)
                    )
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
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            val trimmed = emailInput.trim()
                            if (trimmed.contains("@") && trimmed.contains(".")) {
                                onSaveEmail(trimmed)
                                onDismiss()
                            } else {
                                errorText = "Please enter a valid email address"
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CrimsonRed),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Save", color = Color.White)
                    }
                }
            }
        }
    }
}
