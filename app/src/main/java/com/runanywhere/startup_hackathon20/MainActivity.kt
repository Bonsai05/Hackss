package com.runanywhere.startup_hackathon20

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.runanywhere.startup_hackathon20.data.UserPreferences
import com.runanywhere.startup_hackathon20.notifications.NotificationHelper
import com.runanywhere.startup_hackathon20.ui.components.BottomNavBar
import com.runanywhere.startup_hackathon20.ui.components.ToastHost
import com.runanywhere.startup_hackathon20.ui.components.rememberToastState
import com.runanywhere.startup_hackathon20.ui.screens.AddEntryScreen
import com.runanywhere.startup_hackathon20.ui.screens.HomeScreen
import com.runanywhere.startup_hackathon20.ui.screens.MoodStatsScreen
import com.runanywhere.startup_hackathon20.ui.screens.SettingsScreen
import com.runanywhere.startup_hackathon20.ui.screens.SplashScreen
import com.runanywhere.startup_hackathon20.ui.theme.MindMirrorTheme
import com.runanywhere.startup_hackathon20.viewmodel.JournalViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setup notifications
        NotificationHelper.createNotificationChannel(this)
        NotificationHelper.scheduleDailyNotification(this, hour = 20, minute = 0) // 8 PM daily

        enableEdgeToEdge()
        setContent {
            MindMirrorApp()
        }
    }
}

@Composable
fun MindMirrorApp() {
    val context = LocalContext.current
    val userPreferences = UserPreferences(context)
    val isDarkMode by userPreferences.darkModeFlow.collectAsState(initial = false)
    val coroutineScope = rememberCoroutineScope()

    var showSplash by remember { mutableStateOf(true) }

    MindMirrorTheme(darkTheme = isDarkMode) {
        if (showSplash) {
            SplashScreen(onTimeout = { showSplash = false })
        } else {
            MainContent(
                isDarkMode = isDarkMode,
                onToggleDarkMode = {
                    coroutineScope.launch {
                        userPreferences.toggleDarkMode()
                    }
                }
            )
        }
    }
}

@Composable
fun MainContent(
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit
) {
    val toastState = rememberToastState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            val navController = rememberNavController()
            val viewModel: JournalViewModel = viewModel()

            val entries by viewModel.entries.collectAsState()
            val detectedEmotion by viewModel.detectedEmotion.collectAsState()
            val affirmation by viewModel.affirmation.collectAsState()
            val isProcessing by viewModel.isProcessing.collectAsState()

            Scaffold(
                bottomBar = {
                    BottomNavBar(navController = navController)
                }
            ) { paddingValues ->
                NavHost(
                    navController = navController,
                    startDestination = "home",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    composable("home") {
                        HomeScreen(
                            entries = entries,
                            onAddEntryClick = {
                                viewModel.resetEmotion()
                                navController.navigate("add_entry")
                            },
                            onDeleteEntry = { entryId ->
                                viewModel.deleteEntry(entryId)
                                toastState.showToast("Entry deleted")
                            },
                            onSettingsClick = {
                                navController.navigate("settings")
                            }
                        )
                    }

                    composable("add_entry") {
                        AddEntryScreen(
                            onNavigateBack = {
                                navController.popBackStack()
                            },
                            onAnalyzeEmotion = { text ->
                                viewModel.analyzeEmotion(text)
                            },
                            onSaveEntry = { text, emotion ->
                                viewModel.saveEntry(text, emotion)
                                toastState.showToast("✨ Journal entry saved!")
                                // Navigation is handled by AddEntryScreen
                            },
                            detectedEmotion = detectedEmotion,
                            affirmation = affirmation,
                            isProcessing = isProcessing
                        )
                    }

                    composable("mood_stats") {
                        MoodStatsScreen(entries = entries)
                    }

                    composable("settings") {
                        SettingsScreen(
                            isDarkMode = isDarkMode,
                            onNavigateBack = {
                                navController.popBackStack()
                            },
                            onToggleDarkMode = onToggleDarkMode
                        )
                    }
                }
            }

            // Toast overlay
            ToastHost(toastState = toastState)
        }
    }
}
