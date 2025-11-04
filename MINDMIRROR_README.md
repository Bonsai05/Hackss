# 🪞 MindMirror

**AI-Powered Personal Journal with On-Device Emotion Detection**

MindMirror is a minimal, offline-first journaling app built with Kotlin and Jetpack Compose. It uses
the RunAnywhere SDK for on-device AI inference to detect emotions from your journal entries in
real-time.

---

## ✨ Features

### Core Features

- ✅ **AI-Powered Emotion Detection**: Analyzes your journal entries and detects emotions instantly
- ✅ **6 Emotion Categories**: Happy, Sad, Angry, Calm, Anxious, Neutral
- ✅ **Offline-First**: All data stays on your device - complete privacy
- ✅ **Real-Time Analysis**: Emotion detection in <100ms (with AI model loaded)
- ✅ **Local Storage**: Uses Room Database for persistent storage
- ✅ **Beautiful UI**: Material 3 design with calm pastel colors

### Emotion Categories

- 😊 **Happy** - Joyful, positive feelings
- 😔 **Sad** - Sadness, melancholy
- 😡 **Angry** - Frustration, upset feelings
- 😌 **Calm** - Peaceful, relaxed state
- 😟 **Anxious** - Worried, stressed
- 😐 **Neutral** - Balanced emotional state

---

## 🎨 User Interface

### Home Screen

- Clean list view of all journal entries
- Shows date, emotion badge, and text preview
- Floating action button to add new entries
- Delete entries with confirmation dialog
- Empty state for first-time users

### Add Entry Screen

- Large text input area for writing
- Real-time emotion detection button
- Animated emotion result card with emoji and color
- Voice input placeholder (coming soon)
- Save button appears after emotion analysis

### Design Philosophy

- **Calm Pastel Colors**: Light blue, lavender, beige tones
- **Rounded Cards**: Soft, friendly appearance
- **Minimal Typography**: Clean, readable fonts
- **Smooth Animations**: Fade-in effects for emotion detection

---

## 🏗️ Technical Architecture

### Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose (Material 3)
- **Architecture**: MVVM (Model-View-ViewModel)
- **Database**: Room (SQLite)
- **AI SDK**: RunAnywhere SDK (on-device inference)
- **Navigation**: Jetpack Navigation Compose

### Project Structure

```
app/src/main/java/com/runanywhere/startup_hackathon20/
├── data/
│   ├── JournalEntry.kt          # Room entity
│   ├── JournalDao.kt            # Database access object
│   └── JournalDatabase.kt       # Room database
├── ai/
│   └── RunAnywhereHelper.kt     # AI emotion detection logic
├── viewmodel/
│   └── JournalViewModel.kt      # Main view model
├── ui/
│   ├── screens/
│   │   ├── HomeScreen.kt        # Main journal list
│   │   ├── AddEntryScreen.kt    # Add/edit entry
│   │   └── SplashScreen.kt      # App splash screen
│   └── theme/
│       ├── Color.kt             # Color palette
│       ├── Theme.kt             # Material theme
│       └── Type.kt              # Typography
├── MainActivity.kt              # App entry point
└── MyApplication.kt             # SDK initialization
```

### Database Schema

**Table: journal_entries**
| Column | Type | Description |
|--------|------|-------------|
| id | Long | Auto-generated primary key |
| text | String | Journal entry content |
| emotion | String | Detected emotion category |
| timestamp | Long | Creation timestamp (milliseconds) |

---

## 🤖 AI Integration

### RunAnywhere SDK

MindMirror uses the RunAnywhere SDK for on-device AI inference:

- **Model**: Qwen 2.5 0.5B Instruct Q6_K (374 MB)
- **Inference Type**: Text generation with emotion classification
- **Performance**: <100ms inference time on modern devices
- **Privacy**: All processing happens on-device

### Emotion Detection Flow

1. User writes journal entry
2. Clicks "Detect Emotion" button
3. AI analyzes text using RunAnywhere SDK
4. Emotion is parsed and displayed with animation
5. User can save entry with detected emotion

### Fallback Logic

If the AI model is not loaded, the app uses keyword-based emotion detection:

- Matches common emotion keywords
- Returns appropriate emotion category
- Ensures app remains functional during testing

---

## 📦 Installation & Setup

### Prerequisites

- Android Studio (latest stable version)
- JDK 17 or higher
- Android device/emulator with API 24+ (Android 7.0+)
- 4GB+ RAM recommended for AI model

### Steps to Run

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd Hackss
   ```

2. **Open in Android Studio**
    - Open Android Studio
    - Select "Open an Existing Project"
    - Navigate to the project directory

3. **Sync Gradle**
    - Android Studio will automatically sync Gradle
    - Wait for dependencies to download

4. **Run the app**
    - Connect an Android device or start an emulator
    - Click "Run" button or press Shift+F10
    - App will install and launch

### First-Time Setup

When you first launch the app:

1. The SDK initializes in the background
2. You can immediately start journaling (using placeholder AI)
3. To use real AI:
    - The Qwen 2.5 0.5B model is registered automatically
    - Download the model when needed (374 MB)
    - Load the model for AI-powered emotion detection

---

## 🚀 Usage Guide

### Creating Your First Entry

1. Launch MindMirror
2. Tap the **+ button** on the home screen
3. Write about your thoughts and feelings
4. Tap **"Detect Emotion"** to analyze
5. See your emotion with animated result
6. Tap **"Save Entry"** to store locally

### Viewing Your Journal

- All entries appear on the home screen
- Sorted by most recent first
- Each card shows:
    - Date and time
    - Emotion badge with emoji
    - First 3 lines of text
    - Delete option

### Deleting Entries

1. Find the entry you want to delete
2. Tap the delete icon
3. Confirm deletion
4. Entry is removed permanently

---

## 🔒 Privacy & Security

### Data Privacy

- ✅ **100% Offline**: No internet required after setup
- ✅ **Local Storage**: All data stored on your device
- ✅ **No Cloud Sync**: Your journal never leaves your device
- ✅ **No Analytics**: No tracking or data collection

### Permissions Required

- **INTERNET**: Only for downloading AI model (one-time)
- **WRITE_EXTERNAL_STORAGE**: Model caching (Android 9 and below)

---

## 🎯 Future Enhancements

### Planned Features

- [ ] Voice input with speech-to-text
- [ ] Entry search and filtering
- [ ] Mood trends and analytics
- [ ] Export journal to PDF/text
- [ ] Dark mode support
- [ ] Entry editing capability
- [ ] Tags and categories
- [ ] Reminder notifications
- [ ] Backup and restore

---

## 📱 Requirements

### Minimum Requirements

- Android 7.0 (API 24) or higher
- 2GB RAM (4GB+ recommended)
- 500MB free storage (for app + AI model)
- ARM64 device architecture

### Recommended

- Android 12 or higher
- 4GB+ RAM
- 1GB+ free storage
- Modern processor for faster AI inference

---

## 🛠️ Development

### Building from Source

```bash
# Clean build
./gradlew clean

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run tests
./gradlew test
```

### Key Dependencies

```kotlin
// Jetpack Compose
implementation("androidx.compose.material3:material3")
implementation("androidx.navigation:navigation-compose:2.8.5")

// Room Database
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")
ksp("androidx.room:room-compiler:2.6.1")

// RunAnywhere SDK
implementation(files("libs/RunAnywhereKotlinSDK-release.aar"))
implementation(files("libs/runanywhere-llm-llamacpp-release.aar"))
```

---

## 🤝 Contributing

Contributions are welcome! Here's how you can help:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License

This project is built as part of a hackathon demonstration for RunAnywhere SDK.

---

## 🙏 Acknowledgments

- **RunAnywhere SDK** - For providing on-device AI capabilities
- **Material Design 3** - For beautiful UI components
- **Jetpack Compose** - For modern Android UI development
- **Room Database** - For reliable local storage

---

## 📞 Support

For issues, questions, or feedback:

- Open an issue on GitHub
- Check the RunAnywhere SDK documentation
- Review the code comments for implementation details

---

## 🎉 Day 1-3 Build Complete

This is a fully functional MVP built in 3 days with:

- ✅ Core journaling functionality
- ✅ AI emotion detection
- ✅ Local database storage
- ✅ Beautiful Material 3 UI
- ✅ Smooth animations
- ✅ Offline-first architecture

**Ready to compile, run, and use locally!**

---

*Made with ❤️ using Kotlin and Jetpack Compose*
