package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.NewsCategory
import com.example.ui.theme.CrimsonDark
import com.example.ui.theme.CrimsonLight
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.GoldCoin
import com.example.ui.theme.GoldCoinDark
import com.example.ui.theme.GoldCoinLight
import com.example.ui.theme.NeutralDark
import com.example.ui.theme.NeutralMedium
import com.example.ui.theme.SaffronOrange
import com.example.ui.theme.SuccessGreen

data class ImagePreset(
    val label: String,
    val url: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublishArticleScreen(
    currentEmail: String,
    isOwner: Boolean,
    onBackClick: () -> Unit,
    onUnlockWithPasskey: (String) -> Boolean,
    onPublish: (
        title: String,
        summary: String,
        content: List<String>,
        bulletPoints: List<String>,
        category: NewsCategory,
        author: String,
        imageUrl: String,
        city: String?,
        isBreaking: Boolean
    ) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var summary by remember { mutableStateOf("") }
    var contentText by remember { mutableStateOf("") }
    var bulletPointsText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(NewsCategory.NEWS) }
    var author by remember { mutableStateOf("Hariom Awasthi (Owner / Editor-in-Chief)") }
    var city by remember { mutableStateOf("Global") }
    var isBreaking by remember { mutableStateOf(false) }

    val imagePresets = remember {
        listOf(
            ImagePreset("Marvel / Sci-Fi", "https://images.unsplash.com/photo-1607604276583-eef5d076aa5f?w=800&q=80"),
            ImagePreset("Tech & AI", "https://images.unsplash.com/photo-1518770660439-4636190af475?w=800&q=80"),
            ImagePreset("World Breaking", "https://images.unsplash.com/photo-1585829365295-ab7cd400c167?w=800&q=80"),
            ImagePreset("Finance & Markets", "https://images.unsplash.com/photo-1611974789855-9c2a0a7236a3?w=800&q=80"),
            ImagePreset("Health & Wellness", "https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?w=800&q=80"),
            ImagePreset("Digital Marketing", "https://images.unsplash.com/photo-1460925895917-afdab827c52f?w=800&q=80"),
            ImagePreset("Trending Pop Culture", "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=800&q=80")
        )
    }

    var selectedImageUrl by remember { mutableStateOf(imagePresets[0].url) }
    var customImageUrl by remember { mutableStateOf("") }
    var isUsingCustomUrl by remember { mutableStateOf(false) }

    var titleError by remember { mutableStateOf<String?>(null) }
    var contentError by remember { mutableStateOf<String?>(null) }
    var passkeyInput by remember { mutableStateOf("") }
    var passkeyError by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Campaign,
                            contentDescription = null,
                            tint = GoldCoin,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Owner Studio • Publish Update",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = Color.White
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick, modifier = Modifier.testTag("publish_back_button")) {
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
                .testTag("publish_article_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Owner Status Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = if (isOwner) GoldCoinLight else CrimsonLight),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(if (isOwner) GoldCoinDark else CrimsonRed),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isOwner) Icons.Default.Verified else Icons.Default.Lock,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isOwner) "👑 Verified App Owner" else "🔒 Owner Privileges Required",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = NeutralDark
                            )
                            Text(
                                text = if (isOwner) {
                                    "Logged in as: $currentEmail\nAny update you publish goes live on the public feed instantly for all users."
                                } else {
                                    "Only the verified app owner ($currentEmail) is allowed to publish live updates."
                                },
                                fontSize = 11.sp,
                                color = NeutralMedium,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }

            // If not owner, show passkey entry to unlock
            if (!isOwner) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Unlock Owner Publishing",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = NeutralDark
                            )
                            Text(
                                text = "Enter the Owner Passkey (Default: 7100) or switch your email to hariomawasthi710@gmail.com in the wallet/profile screen.",
                                fontSize = 12.sp,
                                color = NeutralMedium,
                                modifier = Modifier.padding(vertical = 6.dp)
                            )

                            OutlinedTextField(
                                value = passkeyInput,
                                onValueChange = {
                                    passkeyInput = it
                                    passkeyError = null
                                },
                                label = { Text("Owner Passkey") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )

                            if (passkeyError != null) {
                                Text(
                                    text = passkeyError!!,
                                    color = CrimsonRed,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Button(
                                onClick = {
                                    val success = onUnlockWithPasskey(passkeyInput)
                                    if (!success) {
                                        passkeyError = "Invalid passkey. Please try 7100."
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = CrimsonRed),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Unlock Owner Studio", color = Color.White)
                            }
                        }
                    }
                }
            } else {
                // Owner is verified: Show full publication form

                // 1. Article Headline
                item {
                    Column {
                        Text(
                            text = "1. Article Headline / Title *",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = NeutralDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = title,
                            onValueChange = {
                                title = it
                                titleError = null
                            },
                            placeholder = { Text("e.g. Marvel Announces New Live-Action Movie...") },
                            isError = titleError != null,
                            supportingText = titleError?.let { { Text(it, color = CrimsonRed) } },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("publish_title_input")
                        )
                    }
                }

                // 2. Category Selection
                item {
                    Column {
                        Text(
                            text = "2. Select News Category *",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = NeutralDark
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(NewsCategory.entries) { category ->
                                val selected = selectedCategory == category
                                FilterChip(
                                    selected = selected,
                                    onClick = { selectedCategory = category },
                                    label = { Text(category.titleEnglish, fontSize = 12.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = CrimsonRed,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }
                }

                // 3. Short Summary
                item {
                    Column {
                        Text(
                            text = "3. Short Summary (1-2 sentences)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = NeutralDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = summary,
                            onValueChange = { summary = it },
                            placeholder = { Text("Brief introduction that appears in the card feed...") },
                            maxLines = 3,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("publish_summary_input")
                        )
                    }
                }

                // 4. Full Article Story Content
                item {
                    Column {
                        Text(
                            text = "4. Full Story Content *",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = NeutralDark
                        )
                        Text(
                            text = "Separate paragraphs with a blank line. Users read this to earn their 2-minute coin.",
                            fontSize = 11.sp,
                            color = NeutralMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = contentText,
                            onValueChange = {
                                contentText = it
                                contentError = null
                            },
                            placeholder = {
                                Text(
                                    "Enter the detailed article paragraphs here...\n\nParagraph 2 with more context and facts..."
                                )
                            },
                            minLines = 4,
                            maxLines = 8,
                            isError = contentError != null,
                            supportingText = contentError?.let { { Text(it, color = CrimsonRed) } },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("publish_content_input")
                        )
                    }
                }

                // 5. Bullet Points / Highlights
                item {
                    Column {
                        Text(
                            text = "5. Key Highlights / Bullet Points",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = NeutralDark
                        )
                        Text(
                            text = "Enter each bullet point on a new line (optional):",
                            fontSize = 11.sp,
                            color = NeutralMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = bulletPointsText,
                            onValueChange = { bulletPointsText = it },
                            placeholder = { Text("Point 1: Major discovery announced\nPoint 2: Effective from next month\nPoint 3: Public reaction") },
                            minLines = 3,
                            maxLines = 5,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("publish_bullets_input")
                        )
                    }
                }

                // 6. Featured Image Picker
                item {
                    Column {
                        Text(
                            text = "6. Featured Image",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = NeutralDark
                        )
                        Text(
                            text = "Choose a preset or enter a custom web image URL:",
                            fontSize = 11.sp,
                            color = NeutralMedium
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(imagePresets) { preset ->
                                val isSelected = !isUsingCustomUrl && selectedImageUrl == preset.url
                                Card(
                                    modifier = Modifier
                                        .width(130.dp)
                                        .clickable {
                                            selectedImageUrl = preset.url
                                            isUsingCustomUrl = false
                                        },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSelected) CrimsonLight else MaterialTheme.colorScheme.surface
                                    ),
                                    elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 3.dp else 1.dp)
                                ) {
                                    Column {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(70.dp)
                                        ) {
                                            AsyncImage(
                                                model = preset.url,
                                                contentDescription = preset.label,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier.fillMaxSize()
                                            )
                                            if (isSelected) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(22.dp)
                                                        .align(Alignment.TopEnd)
                                                        .padding(2.dp)
                                                        .clip(CircleShape)
                                                        .background(CrimsonRed),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Check,
                                                        contentDescription = null,
                                                        tint = Color.White,
                                                        modifier = Modifier.size(14.dp)
                                                    )
                                                }
                                            }
                                        }
                                        Text(
                                            text = preset.label,
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) CrimsonRed else NeutralDark,
                                            modifier = Modifier.padding(6.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Custom URL Option
                        OutlinedTextField(
                            value = customImageUrl,
                            onValueChange = {
                                customImageUrl = it
                                if (it.isNotBlank()) isUsingCustomUrl = true
                            },
                            label = { Text("Or paste Custom Image URL (https://...)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // 7. Metadata: Author, City, Breaking toggle
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            OutlinedTextField(
                                value = author,
                                onValueChange = { author = it },
                                label = { Text("Author / Reporter Byline") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = city,
                                onValueChange = { city = it },
                                label = { Text("City / Location") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "🚨 Mark as BREAKING NEWS",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = CrimsonRed
                                    )
                                    Text(
                                        text = "Will pin to the top ticker alert banner on Home screen",
                                        fontSize = 11.sp,
                                        color = NeutralMedium
                                    )
                                }

                                Switch(
                                    checked = isBreaking,
                                    onCheckedChange = { isBreaking = it },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = CrimsonRed
                                    )
                                )
                            }
                        }
                    }
                }

                // 8. Publish Button
                item {
                    Button(
                        onClick = {
                            var hasError = false
                            if (title.trim().isEmpty()) {
                                titleError = "Please enter an article headline."
                                hasError = true
                            }
                            if (contentText.trim().isEmpty()) {
                                contentError = "Please enter the article content."
                                hasError = true
                            }
                            if (hasError) return@Button

                            val paragraphs = contentText.split("\n\n")
                                .map { it.trim() }
                                .filter { it.isNotEmpty() }
                                .ifEmpty { listOf(contentText.trim()) }

                            val bullets = bulletPointsText.split("\n")
                                .map { it.trim() }
                                .filter { it.isNotEmpty() }

                            val finalImage = if (isUsingCustomUrl && customImageUrl.isNotBlank()) {
                                customImageUrl.trim()
                            } else {
                                selectedImageUrl
                            }

                            onPublish(
                                title.trim(),
                                summary.trim(),
                                paragraphs,
                                bullets,
                                selectedCategory,
                                author.trim(),
                                finalImage,
                                city.trim(),
                                isBreaking
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CrimsonRed),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("publish_submit_button")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Campaign,
                                contentDescription = null,
                                tint = GoldCoin,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "🚀 Publish Live Update to Public Feed",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
