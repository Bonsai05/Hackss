package com.runanywhere.startup_hackathon20.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.runanywhere.startup_hackathon20.ai.RunAnywhereHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEntryScreen(
    onNavigateBack: () -> Unit,
    onAnalyzeEmotion: (String) -> Unit,
    onSaveEntry: (String, String) -> Unit,
    detectedEmotion: String?,
    affirmation: String? = null,
    isProcessing: Boolean
) {
    var entryText by remember { mutableStateOf("") }

    // Show save button when emotion is detected and not processing
    val showAnalyzeButton = detectedEmotion == null && !isProcessing
    val showSaveButton = detectedEmotion != null && !isProcessing

    // Background gradient based on emotion
    val backgroundColorValue = detectedEmotion?.let {
        Color(RunAnywhereHelper.getEmotionBackgroundColor(it))
    } ?: MaterialTheme.colorScheme.background

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            backgroundColorValue.copy(alpha = 0.3f),
            MaterialTheme.colorScheme.background
        )
    )

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier.clip(
                    RoundedCornerShape(
                        bottomStart = 16.dp,
                        bottomEnd = 16.dp
                    )
                ),
                shadowElevation = 4.dp
            ) {
                TopAppBar(
                    title = {
                        Text(
                            "New Journal Entry",
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(brush = backgroundBrush)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Instructions
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                    )
                ) {
                    Text(
                        text = "✨ Write about your thoughts and feelings. AI will detect your emotion instantly!",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Text Input
                OutlinedTextField(
                    value = entryText,
                    onValueChange = { entryText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 200.dp),
                    placeholder = { Text("How are you feeling today?") },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    ),
                    maxLines = 12
                )

                // Voice Input Button (Placeholder for future feature)
                OutlinedButton(
                    onClick = { /* TODO: Implement voice input */ },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                ) {
                    Text("🎤 Voice Input (Coming Soon)")
                }

                // Analyze Button (shown when no emotion detected)
                AnimatedVisibility(
                    visible = showAnalyzeButton,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Button(
                        onClick = {
                            if (entryText.isNotBlank()) {
                                onAnalyzeEmotion(entryText)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = entryText.isNotBlank(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("✨ Detect Emotion", fontSize = 16.sp)
                    }
                }

                // Processing Indicator
                AnimatedVisibility(
                    visible = isProcessing,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "Analyzing your emotions...",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // Emotion Result with Animation
                AnimatedVisibility(
                    visible = detectedEmotion != null,
                    enter = fadeIn(animationSpec = tween(500)) + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    detectedEmotion?.let { emotion ->
                        EmotionResultCard(emotion = emotion)
                    }
                }

                // Affirmation Card with Animation
                AnimatedVisibility(
                    visible = affirmation != null,
                    enter = fadeIn(
                        animationSpec = tween(
                            800,
                            delayMillis = 300
                        )
                    ) + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    affirmation?.let { message ->
                        AffirmationCard(affirmation = message)
                    }
                }

                // Save Button (show after emotion detection)
                AnimatedVisibility(
                    visible = showSaveButton,
                    enter = fadeIn(
                        animationSpec = tween(
                            600,
                            delayMillis = 500
                        )
                    ) + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Save button
                        Button(
                            onClick = {
                                detectedEmotion?.let { emotion ->
                                    onSaveEntry(entryText, emotion)
                                    onNavigateBack()
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.tertiary
                            )
                        ) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = "Save",
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "💾 Save to Journal",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Re-analyze button (optional)
                        OutlinedButton(
                            onClick = {
                                if (entryText.isNotBlank()) {
                                    onAnalyzeEmotion(entryText)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("🔄 Re-analyze Emotion", fontSize = 14.sp)
                        }
                    }
                }

                // Add extra spacing at the bottom for better scrolling
                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}

@Composable
fun EmotionResultCard(emotion: String) {
    val emoji = RunAnywhereHelper.getEmotionEmoji(emotion)
    val color = Color(RunAnywhereHelper.getEmotionColor(emotion))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.15f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Detected Emotion",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            Text(
                text = emoji,
                fontSize = 64.sp
            )

            Text(
                text = emotion,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = color.copy(alpha = 0.3f)
            ) {
                Text(
                    text = getEmotionDescription(emotion),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun AffirmationCard(affirmation: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "💭",
                fontSize = 32.sp
            )

            Text(
                text = "Affirmation",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                fontWeight = FontWeight.Medium
            )

            Text(
                text = affirmation,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

fun getEmotionDescription(emotion: String): String {
    return when (emotion) {
        "Happy" -> "You're feeling joyful and positive"
        "Sad" -> "You're experiencing sadness or melancholy"
        "Angry" -> "You're feeling frustrated or upset"
        "Calm" -> "You're in a peaceful and relaxed state"
        "Anxious" -> "You're feeling worried or stressed"
        "Neutral" -> "You're in a balanced emotional state"
        else -> "Emotion detected"
    }
}
