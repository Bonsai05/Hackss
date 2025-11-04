package com.runanywhere.startup_hackathon20.data

import kotlin.random.Random

/**
 * Affirmations and reflection tips for each emotion
 */
object Affirmations {

    private val affirmationsMap = mapOf(
        "Happy" to listOf(
            "Keep spreading your light! ✨",
            "Celebrate small wins today. 🎉",
            "Your joy is contagious! 😊",
            "You deserve this happiness. 💛",
            "Embrace this beautiful moment. 🌟"
        ),
        "Sad" to listOf(
            "It's okay to slow down. 🌙",
            "Healing takes time — breathe. 💙",
            "Your feelings are valid. 🤗",
            "Tomorrow is a new day. 🌅",
            "Be gentle with yourself. 💙"
        ),
        "Angry" to listOf(
            "Pause before reacting. ⏸️",
            "Channel that energy into something creative. 🎨",
            "Your feelings matter. Take a deep breath. 🌬️",
            "It's okay to feel this way. 💪",
            "Step back and find your center. 🧘"
        ),
        "Calm" to listOf(
            "Enjoy this peaceful moment. 🌿",
            "You've found your balance. ⚖️",
            "Breathe in the tranquility. 🍃",
            "This serenity is yours to keep. 🕊️",
            "Stay grounded in this calm. 🌊"
        ),
        "Anxious" to listOf(
            "One step at a time. You've got this. 💜",
            "Breathe deeply. You are safe. 🫁",
            "This feeling will pass. 🌈",
            "Focus on what you can control. 🎯",
            "You've overcome challenges before. 💪"
        ),
        "Neutral" to listOf(
            "Every day doesn't need to be extraordinary. ✨",
            "Being present is enough. 🧘",
            "Balance is beautiful. ⚖️",
            "Steady days build strong foundations. 🏛️",
            "Peace in simplicity. 🌾"
        )
    )

    /**
     * Get a random affirmation for the given emotion
     */
    fun getRandomAffirmation(emotion: String): String {
        val affirmations = affirmationsMap[emotion] ?: affirmationsMap["Neutral"]!!
        return affirmations[Random.nextInt(affirmations.size)]
    }

    /**
     * Get all affirmations for an emotion
     */
    fun getAffirmations(emotion: String): List<String> {
        return affirmationsMap[emotion] ?: affirmationsMap["Neutral"]!!
    }
}
