# 🚀 MindMirror - Quick Start Guide

Get MindMirror running on your device in 5 minutes!

---

## 📋 Prerequisites

- ✅ Android Studio (latest stable)
- ✅ Android device or emulator (API 24+)
- ✅ 4GB+ RAM recommended
- ✅ Internet connection (for initial model download)

---

## ⚡ Quick Setup

### 1. Open the Project

```bash
# Open in Android Studio
File → Open → Select the "Hackss" folder
```

### 2. Sync Gradle

- Wait for Android Studio to sync dependencies
- This may take 2-3 minutes on first run

### 3. Run the App

- Connect your device or start an emulator
- Click the green "Run" button (▶️)
- Or press `Shift + F10`

### 4. Test Without AI (Instant)

The app works immediately with placeholder emotion detection:

1. Tap the **+ button**
2. Write: "I'm feeling great today!"
3. Tap **"Detect Emotion"**
4. See **Happy** emotion detected instantly
5. Tap **"Save Entry"**

✨ The app uses keyword-based fallback when AI model isn't loaded.

---

## 🤖 Enable Real AI (Optional)

For actual AI-powered emotion detection:

### Step 1: Wait for SDK Initialization

- The SDK initializes automatically on first launch
- Check logcat for: `"SDK initialized successfully"`

### Step 2: Download the Model

Currently, the app auto-registers the Qwen 2.5 0.5B model (374 MB). To download:

**Option A: Add Model Management Screen (Future)**

- Navigate to a model management screen
- Download the Qwen model
- Load the model

**Option B: Use Existing ChatViewModel (Temporary)**
You can use the ChatViewModel from the original template to download models.

### Step 3: Test with AI

Once model is loaded:

1. Write a journal entry
2. Click "Detect Emotion"
3. AI analyzes the text (takes 1-2 seconds)
4. More accurate emotion detection!

---

## 🎨 Key Features to Test

### 1. Add Journal Entry

- Tap + button
- Write your thoughts
- Detect emotion
- Save entry

### 2. View Entries

- Scroll through your journal
- See emotion badges
- View date and preview

### 3. Delete Entry

- Tap delete icon on any entry
- Confirm deletion
- Entry removed

### 4. Different Emotions

Try writing text to trigger different emotions:

**Happy**: "I'm so excited about this amazing day!"
**Sad**: "I miss my old friends and feel lonely"
**Angry**: "I'm so frustrated with everything right now"
**Calm**: "I feel peaceful and relaxed today"
**Anxious**: "I'm worried and stressed about tomorrow"
**Neutral**: "Today was an ordinary day"

---

## 🔧 Project Structure Overview

```
MindMirror/
├── data/
│   ├── JournalEntry.kt      # Database entity
│   ├── JournalDao.kt        # CRUD operations
│   └── JournalDatabase.kt   # Room database
│
├── ai/
│   └── RunAnywhereHelper.kt # AI logic + fallback
│
├── viewmodel/
│   └── JournalViewModel.kt  # State management
│
├── ui/screens/
│   ├── HomeScreen.kt        # Main screen
│   ├── AddEntryScreen.kt    # Add entry
│   └── SplashScreen.kt      # Splash screen
│
├── ui/theme/
│   ├── Color.kt             # Pastel colors
│   ├── Theme.kt             # Material 3 theme
│   └── Type.kt              # Typography
│
├── MainActivity.kt          # App entry + navigation
└── MyApplication.kt         # SDK initialization
```

---

## 🐛 Troubleshooting

### App Won't Build

```bash
# Clean and rebuild
./gradlew clean
./gradlew build
```

### Room Database Errors

- Delete app from device/emulator
- Reinstall (clears old database)

### Navigation Errors

If you see "Unresolved reference: navigation":

- File → Sync Project with Gradle Files
- Wait for sync to complete
- Rebuild project

### AI Not Working

- Check logcat for SDK initialization status
- Fallback emotion detection works without AI
- AI requires model download + load

---

## 📱 Testing Checklist

- [ ] App launches successfully
- [ ] Home screen shows empty state
- [ ] Can navigate to add entry screen
- [ ] Can write journal entry
- [ ] Emotion detection works (fallback or AI)
- [ ] Entry saves successfully
- [ ] Entry appears on home screen
- [ ] Entry shows correct emotion badge
- [ ] Can delete entry
- [ ] Delete confirmation works

---

## 🎯 Next Steps

### For Users

1. Start journaling daily
2. Track your emotional patterns
3. Enjoy the privacy of offline storage

### For Developers

1. Review the code structure
2. Explore RunAnywhereHelper.kt for AI logic
3. Check JournalViewModel.kt for state management
4. Customize colors in Color.kt
5. Add new features (see MINDMIRROR_README.md)

---

## 📚 Additional Resources

- **Full README**: See `MINDMIRROR_README.md` for complete documentation
- **RunAnywhere SDK**: See `RUNANYWHERE_SDK_COMPLETE_GUIDE.md` for SDK details
- **Code Comments**: All files have detailed comments

---

## 🎉 You're Ready!

MindMirror is now running on your device. Start journaling and exploring your emotions!

**Questions?**

- Check the full README
- Review code comments
- Open an issue on GitHub

---

*Happy Journaling! 📝✨*
