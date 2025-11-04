package com.runanywhere.startup_hackathon20.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.runanywhere.sdk.public.RunAnywhere
import com.runanywhere.sdk.public.extensions.addModelFromURL
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Model Download States
 */
sealed class ModelDownloadState {
    object NotStarted : ModelDownloadState()
    object Downloading : ModelDownloadState()
    data class Progress(val percentage: Int, val downloaded: Long, val total: Long) :
        ModelDownloadState()

    object Completed : ModelDownloadState()
    data class Failed(val error: String) : ModelDownloadState()
    object Paused : ModelDownloadState()
}

/**
 * Model Information
 */
data class ModelInfo(
    val name: String,
    val url: String,
    val size: Long, // in bytes
    val description: String,
    val isDownloaded: Boolean = false
)

/**
 * ViewModel to manage AI model downloads
 */
class ModelDownloadViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "ModelDownloadVM"

        // Model details
        const val MODEL_NAME = "Qwen 2.5 0.5B Instruct Q6_K"
        const val MODEL_URL =
            "https://huggingface.co/Triangle104/Qwen2.5-0.5B-Instruct-Q6_K-GGUF/resolve/main/qwen2.5-0.5b-instruct-q6_k.gguf"
        const val MODEL_SIZE_MB = 374L
        const val MODEL_SIZE_BYTES = MODEL_SIZE_MB * 1024 * 1024
    }

    // Download state
    private val _downloadState = MutableStateFlow<ModelDownloadState>(ModelDownloadState.NotStarted)
    val downloadState: StateFlow<ModelDownloadState> = _downloadState.asStateFlow()

    // Model information
    private val _modelInfo = MutableStateFlow(
        ModelInfo(
            name = MODEL_NAME,
            url = MODEL_URL,
            size = MODEL_SIZE_BYTES,
            description = "AI model for advanced emotion detection from text",
            isDownloaded = false
        )
    )
    val modelInfo: StateFlow<ModelInfo> = _modelInfo.asStateFlow()

    // Is model available for use
    private val _isModelReady = MutableStateFlow(false)
    val isModelReady: StateFlow<Boolean> = _isModelReady.asStateFlow()

    init {
        checkModelStatus()
    }

    /**
     * Check if model is already downloaded
     */
    fun checkModelStatus() {
        viewModelScope.launch {
            try {
                // Check if model exists in RunAnywhere
                val models = RunAnywhere.getAvailableModels()
                val isDownloaded = models.any { it.name == MODEL_NAME }

                _modelInfo.value = _modelInfo.value.copy(isDownloaded = isDownloaded)
                _isModelReady.value = isDownloaded

                if (isDownloaded) {
                    _downloadState.value = ModelDownloadState.Completed
                    Log.i(TAG, "Model already downloaded: $MODEL_NAME")
                } else {
                    Log.i(TAG, "Model not found: $MODEL_NAME")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error checking model status: ${e.message}")
            }
        }
    }

    /**
     * Start downloading the model
     */
    fun startDownload() {
        if (_downloadState.value is ModelDownloadState.Downloading) {
            Log.w(TAG, "Download already in progress")
            return
        }

        viewModelScope.launch {
            try {
                _downloadState.value = ModelDownloadState.Downloading
                Log.i(TAG, "Starting model download: $MODEL_NAME")

                // Start download with progress tracking
                addModelFromURL(
                    url = MODEL_URL,
                    name = MODEL_NAME,
                    type = "LLM",
                    onProgress = { downloaded, total ->
                        val percentage = if (total > 0) {
                            ((downloaded * 100) / total).toInt()
                        } else {
                            0
                        }

                        _downloadState.value = ModelDownloadState.Progress(
                            percentage = percentage,
                            downloaded = downloaded,
                            total = total
                        )

                        Log.d(TAG, "Download progress: $percentage% ($downloaded / $total bytes)")
                    }
                )

                // Download completed
                _downloadState.value = ModelDownloadState.Completed
                _modelInfo.value = _modelInfo.value.copy(isDownloaded = true)
                _isModelReady.value = true

                Log.i(TAG, "Model download completed: $MODEL_NAME")

            } catch (e: Exception) {
                _downloadState.value = ModelDownloadState.Failed(e.message ?: "Unknown error")
                Log.e(TAG, "Model download failed: ${e.message}", e)
            }
        }
    }

    /**
     * Cancel/Pause download
     */
    fun pauseDownload() {
        viewModelScope.launch {
            try {
                // Note: RunAnywhere SDK may not support pause, this is a placeholder
                _downloadState.value = ModelDownloadState.Paused
                Log.i(TAG, "Download paused")
            } catch (e: Exception) {
                Log.e(TAG, "Error pausing download: ${e.message}")
            }
        }
    }

    /**
     * Delete the downloaded model
     */
    fun deleteModel() {
        viewModelScope.launch {
            try {
                // Delete model from RunAnywhere
                RunAnywhere.deleteModel(MODEL_NAME)

                _modelInfo.value = _modelInfo.value.copy(isDownloaded = false)
                _isModelReady.value = false
                _downloadState.value = ModelDownloadState.NotStarted

                Log.i(TAG, "Model deleted: $MODEL_NAME")
            } catch (e: Exception) {
                Log.e(TAG, "Error deleting model: ${e.message}")
            }
        }
    }

    /**
     * Get download progress as a formatted string
     */
    fun getDownloadProgressText(): String {
        return when (val state = _downloadState.value) {
            is ModelDownloadState.NotStarted -> "Not downloaded"
            is ModelDownloadState.Downloading -> "Downloading..."
            is ModelDownloadState.Progress -> {
                val downloadedMB = state.downloaded / (1024 * 1024)
                val totalMB = state.total / (1024 * 1024)
                "${state.percentage}% ($downloadedMB / $totalMB MB)"
            }

            is ModelDownloadState.Completed -> "Download complete"
            is ModelDownloadState.Failed -> "Failed: ${state.error}"
            is ModelDownloadState.Paused -> "Paused"
        }
    }

    /**
     * Get model size in human-readable format
     */
    fun getModelSizeText(): String {
        val sizeMB = _modelInfo.value.size / (1024 * 1024)
        return "$sizeMB MB"
    }
}
