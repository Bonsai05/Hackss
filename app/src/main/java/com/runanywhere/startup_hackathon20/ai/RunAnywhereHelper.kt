package com.runanywhere.startup_hackathon20.ai

import android.util.Log
import com.runanywhere.sdk.public.RunAnywhere
import kotlin.random.Random

/**
 * Helper class for AI operations using RunAnywhere SDK
 * Handles model initialization and emotion detection
 */
object RunAnywhereHelper {

    private const val TAG = "RunAnywhereHelper"

    // Available emotion categories
    private val EMOTIONS = listOf("Happy", "Sad", "Angry", "Calm", "Anxious", "Neutral")

    /**
     * Predict emotion from text using AI model
     * Uses direct inference via RunAnywhere SDK
     */
    suspend fun predictEmotion(text: String): String {
        return try {
            // Build the emotion detection prompt
            val prompt = buildEmotionDetectionPrompt(text)

            // Direct inference using RunAnywhere SDK
            val emotion = RunAnywhere.generate(prompt)

            // Parse and validate the emotion response
            parseEmotionFromResponse(emotion)

        } catch (e: Exception) {
            Log.w(TAG, "AI inference failed, using fallback: ${e.message}")
            // Fallback to keyword-based detection if AI fails
            predictEmotionPlaceholder(text)
        }
    }

    /**
     * Build a prompt for emotion detection
     */
    private fun buildEmotionDetectionPrompt(text: String): String {
        return """
You are an emotion detection AI. Analyze the following text and respond with ONLY ONE word from this list:
Happy, Sad, Angry, Calm, Anxious, Neutral

Text: "$text"

Emotion:
        """.trimIndent()
    }

    /**
     * Parse emotion from AI response
     */
    private fun parseEmotionFromResponse(response: String): String {
        val normalized = response.trim().lowercase()

        // Try to find a matching emotion in the response
        for (emotion in EMOTIONS) {
            if (normalized.contains(emotion.lowercase())) {
                return emotion
            }
        }

        // Default to Neutral if no match found
        return "Neutral"
    }

    /**
     * Placeholder emotion detection based on simple keyword matching
     * Used when AI model is not available
     */
    private fun predictEmotionPlaceholder(text: String): String {
        val normalized = text.lowercase()

        // Simple keyword-based detection
        return when {
            // Happy indicators
            normalized.containsAny(
                listOf(
                    "happy", "joy", "excited", "great", "wonderful",
                    "amazing", "love", "good", "excellent", "fantastic", "beautiful"
                )
            ) -> "Happy"

            // Sad indicators
            normalized.containsAny(
                listOf(
                    "sad", "depressed", "unhappy", "down", "lonely",
                    "crying", "tears", "miss", "lost", "hurt"
                )
            ) -> "Sad"

            // Angry indicators
            normalized.containsAny(
                listOf(
                    "angry", "mad", "furious", "hate", "rage",
                    "frustrated", "annoyed", "irritated"
                )
            ) -> "Angry"

            // Anxious indicators
            normalized.containsAny(
                listOf(
                    "anxious", "worried", "nervous", "stress",
                    "scared", "fear", "panic", "overwhelm"
                )
            ) -> "Anxious"

            // Calm indicators
            normalized.containsAny(
                listOf(
                    "calm", "peace", "relaxed", "tranquil",
                    "serene", "content", "comfortable", "easy"
                )
            ) -> "Calm"

            // Default
            else -> "Neutral"
        }
    }

    /**
     * Extension function to check if string contains any of the keywords
     */
    private fun String.containsAny(keywords: List<String>): Boolean {
        return keywords.any { this.contains(it) }
    }

    /**
     * Get emoji representation for emotion
     */
    fun getEmotionEmoji(emotion: String): String {
        return when (emotion) {
            "Happy" -> "😊"
            "Sad" -> "😔"
            "Angry" -> "😡"
            "Calm" -> "😌"
            "Anxious" -> "😟"
            "Neutral" -> "😐"
            else -> "😐"
        }
    }

    /**
     * Get color for emotion (as hex string for Compose Color parsing)
     */
    fun getEmotionColor(emotion: String): Long {
        return when (emotion) {
            "Happy" -> 0xFFFFE082    // Yellow
            "Sad" -> 0xFF81D4FA      // Blue
            "Angry" -> 0xFFFF8A65    // Red
            "Calm" -> 0xFFA5D6A7     // Green
            "Anxious" -> 0xFFCE93D8  // Purple
            "Neutral" -> 0xFFCFD8DC  // Grey
            else -> 0xFFCFD8DC
        }
    }

    /**
     * Get light background color for emotion
     */
    fun getEmotionBackgroundColor(emotion: String): Long {
        return when (emotion) {
            "Happy" -> 0xFFFFF9C4    // Light Yellow
            "Sad" -> 0xFFB3E5FC      // Light Blue
            "Angry" -> 0xFFFFCCBC    // Light Red
            "Calm" -> 0xFFC8E6C9     // Light Green
            "Anxious" -> 0xFFE1BEE7  // Light Purple
            "Neutral" -> 0xFFECEFF1  // Light Grey
            else -> 0xFFECEFF1
        }
    }
}
