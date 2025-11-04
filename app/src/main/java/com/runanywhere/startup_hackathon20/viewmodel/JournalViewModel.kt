package com.runanywhere.startup_hackathon20.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.runanywhere.startup_hackathon20.ai.RunAnywhereHelper
import com.runanywhere.startup_hackathon20.data.Affirmations
import com.runanywhere.startup_hackathon20.data.JournalDatabase
import com.runanywhere.startup_hackathon20.data.JournalEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class JournalViewModel(application: Application) : AndroidViewModel(application) {

    private val database = JournalDatabase.getDatabase(application)
    private val journalDao = database.journalDao()

    // Journal entries from database
    val entries: StateFlow<List<JournalEntry>> =
        MutableStateFlow(emptyList<JournalEntry>()).apply {
            viewModelScope.launch {
                journalDao.getAllEntries().collect { entriesList ->
                    value = entriesList
                }
            }
        }

    // UI State
    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()

    private val _detectedEmotion = MutableStateFlow<String?>(null)
    val detectedEmotion: StateFlow<String?> = _detectedEmotion.asStateFlow()

    private val _affirmation = MutableStateFlow<String?>(null)
    val affirmation: StateFlow<String?> = _affirmation.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    /**
     * Analyze text and detect emotion
     */
    fun analyzeEmotion(text: String) {
        if (text.isBlank()) {
            _errorMessage.value = "Please enter some text"
            return
        }

        viewModelScope.launch {
            try {
                _isProcessing.value = true
                _errorMessage.value = null

                // Detect emotion using AI
                val emotion = RunAnywhereHelper.predictEmotion(text)
                _detectedEmotion.value = emotion

                // Get affirmation for the detected emotion
                val affirmation = Affirmations.getRandomAffirmation(emotion)
                _affirmation.value = affirmation

            } catch (e: Exception) {
                _errorMessage.value = "Error analyzing emotion: ${e.message}"
            } finally {
                _isProcessing.value = false
            }
        }
    }

    /**
     * Save journal entry with detected emotion
     */
    fun saveEntry(text: String, emotion: String) {
        if (text.isBlank()) {
            _errorMessage.value = "Cannot save empty entry"
            return
        }

        viewModelScope.launch {
            try {
                val entry = JournalEntry(
                    text = text,
                    emotion = emotion,
                    timestamp = System.currentTimeMillis()
                )
                journalDao.insertEntry(entry)

                // Reset state
                _detectedEmotion.value = null
                _affirmation.value = null

            } catch (e: Exception) {
                _errorMessage.value = "Error saving entry: ${e.message}"
            }
        }
    }

    /**
     * Delete a journal entry
     */
    fun deleteEntry(entryId: Long) {
        viewModelScope.launch {
            try {
                journalDao.deleteEntry(entryId)
            } catch (e: Exception) {
                _errorMessage.value = "Error deleting entry: ${e.message}"
            }
        }
    }

    /**
     * Clear error message
     */
    fun clearError() {
        _errorMessage.value = null
    }

    /**
     * Reset detected emotion and affirmation
     */
    fun resetEmotion() {
        _detectedEmotion.value = null
        _affirmation.value = null
    }
}
