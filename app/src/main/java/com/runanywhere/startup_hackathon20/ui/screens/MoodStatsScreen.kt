package com.runanywhere.startup_hackathon20.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.runanywhere.startup_hackathon20.ai.RunAnywhereHelper
import com.runanywhere.startup_hackathon20.data.JournalEntry
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodStatsScreen(
    entries: List<JournalEntry>
) {
    val scrollState = rememberScrollState()

    // Process entries for the last 7 days
    val moodData = remember(entries) {
        processLast7Days(entries)
    }

    val emotionCounts = remember(entries) {
        countEmotions(entries)
    }

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
                            "Mood Statistics",
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Summary Card
            SummaryCard(entries.size, emotionCounts)

            // 7-Day Chart
            if (moodData.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "7-Day Mood Timeline",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        MoodChart(moodData)

                        // Legend
                        EmotionLegend()
                    }
                }
            } else {
                EmptyStatsCard()
            }

            // Emotion Breakdown
            if (emotionCounts.isNotEmpty()) {
                EmotionBreakdownCard(emotionCounts)
            }
        }
    }
}

@Composable
fun SummaryCard(totalEntries: Int, emotionCounts: Map<String, Int>) {
    val dominantEmotion = emotionCounts.maxByOrNull { it.value }?.key ?: "Neutral"
    val emoji = RunAnywhereHelper.getEmotionEmoji(dominantEmotion)
    val backgroundColor = Color(RunAnywhereHelper.getEmotionBackgroundColor(dominantEmotion))

    // Use dark text color for visibility on light pastel backgrounds
    val textColor = Color(0xFF1C1B1F)  // Dark text for contrast with pastel

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Total Entries",
                    style = MaterialTheme.typography.labelMedium,
                    color = textColor.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "$totalEntries",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = emoji,
                    fontSize = 48.sp
                )
                Text(
                    text = "Most Frequent",
                    style = MaterialTheme.typography.labelSmall,
                    color = textColor.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = dominantEmotion,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            }
        }
    }
}

@Composable
fun MoodChart(moodData: List<DayMood>) {
    val emotions = listOf("Happy", "Sad", "Angry", "Calm", "Anxious", "Neutral")
    val emotionValues = emotions.associateWith { emotion ->
        when (emotion) {
            "Happy" -> 5
            "Calm" -> 4
            "Neutral" -> 3
            "Anxious" -> 2
            "Sad" -> 1
            "Angry" -> 0
            else -> 3
        }
    }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(vertical = 16.dp)
    ) {
        val width = size.width
        val height = size.height
        val padding = 40f
        val chartWidth = width - (padding * 2)
        val chartHeight = height - (padding * 2)

        // Draw grid lines
        for (i in 0..5) {
            val y = padding + (chartHeight / 5) * i
            drawLine(
                color = Color.Gray.copy(alpha = 0.2f),
                start = Offset(padding, y),
                end = Offset(width - padding, y),
                strokeWidth = 1f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
            )
        }

        // Draw data points and lines
        if (moodData.size > 1) {
            val path = Path()
            val pointRadius = 8f

            moodData.forEachIndexed { index, dayMood ->
                val x = padding + (chartWidth / (moodData.size - 1)) * index
                val emotionValue = emotionValues[dayMood.emotion] ?: 3
                val normalizedY = 1 - (emotionValue / 5f)
                val y = padding + (chartHeight * normalizedY)

                if (index == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }

                // Draw point
                val color = Color(RunAnywhereHelper.getEmotionColor(dayMood.emotion))
                drawCircle(
                    color = color,
                    radius = pointRadius,
                    center = Offset(x, y)
                )

                // Draw inner circle
                drawCircle(
                    color = Color.White,
                    radius = pointRadius - 3f,
                    center = Offset(x, y)
                )
            }

            // Draw connecting line
            drawPath(
                path = path,
                color = Color(0xFF9FA8DA),
                style = Stroke(width = 3f)
            )
        }

        // Draw day labels
        moodData.forEachIndexed { index, dayMood ->
            val x = padding + (chartWidth / (moodData.size - 1)) * index
            // Labels would need text drawing which is complex in Canvas
            // Consider using Row with Spacer for labels instead
        }
    }

    // Day labels below chart
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        moodData.forEach { dayMood ->
            Text(
                text = dayMood.dayLabel,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun EmotionLegend() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Emotion Guide",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            listOf("Happy", "Calm", "Neutral").forEach { emotion ->
                LegendItem(emotion, modifier = Modifier.weight(1f))
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            listOf("Anxious", "Sad", "Angry").forEach { emotion ->
                LegendItem(emotion, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun LegendItem(emotion: String, modifier: Modifier = Modifier) {
    val color = Color(RunAnywhereHelper.getEmotionColor(emotion))
    val emoji = RunAnywhereHelper.getEmotionEmoji(emotion)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(12.dp),
            shape = RoundedCornerShape(6.dp),
            color = color
        ) {}

        Text(
            text = "$emoji $emotion",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        )
    }
}

@Composable
fun EmotionBreakdownCard(emotionCounts: Map<String, Int>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Emotion Breakdown",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            emotionCounts.entries.sortedByDescending { it.value }.forEach { (emotion, count) ->
                EmotionBar(emotion, count, emotionCounts.values.maxOrNull() ?: 1)
            }
        }
    }
}

@Composable
fun EmotionBar(emotion: String, count: Int, maxCount: Int) {
    val percentage = if (maxCount > 0) count.toFloat() / maxCount else 0f
    val color = Color(RunAnywhereHelper.getEmotionColor(emotion))
    val emoji = RunAnywhereHelper.getEmotionEmoji(emotion)

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$emoji $emotion",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "$count",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }

        LinearProgressIndicator(
            progress = { percentage },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = color,
            trackColor = color.copy(alpha = 0.2f),
        )
    }
}

@Composable
fun EmptyStatsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "📊",
                fontSize = 48.sp
            )
            Text(
                text = "No Data Yet",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Start journaling to see your mood patterns!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )
        }
    }
}

// Data classes and helper functions
data class DayMood(
    val dayLabel: String,
    val emotion: String,
    val date: Long
)

fun processLast7Days(entries: List<JournalEntry>): List<DayMood> {
    val calendar = Calendar.getInstance()
    val today = calendar.timeInMillis
    val sevenDaysAgo = today - (7 * 24 * 60 * 60 * 1000)

    // Filter entries from last 7 days
    val recentEntries = entries.filter { it.timestamp >= sevenDaysAgo }

    if (recentEntries.isEmpty()) return emptyList()

    // Group by day
    val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())
    val groupedByDay = recentEntries.groupBy { entry ->
        calendar.timeInMillis = entry.timestamp
        calendar.get(Calendar.DAY_OF_YEAR)
    }

    // Get dominant emotion for each day
    val result = mutableListOf<DayMood>()
    for (i in 6 downTo 0) {
        calendar.timeInMillis = today
        calendar.add(Calendar.DAY_OF_YEAR, -i)

        val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
        val dayLabel = dayFormat.format(calendar.time)

        val dayEntries = groupedByDay[dayOfYear]
        val dominantEmotion = dayEntries?.groupBy { it.emotion }
            ?.maxByOrNull { it.value.size }
            ?.key ?: "Neutral"

        result.add(DayMood(dayLabel, dominantEmotion, calendar.timeInMillis))
    }

    return result
}

fun countEmotions(entries: List<JournalEntry>): Map<String, Int> {
    return entries.groupBy { it.emotion }
        .mapValues { it.value.size }
        .toSortedMap()
}
