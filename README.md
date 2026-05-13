# Offline Music Player - MP3

A feature-rich offline music player Android app for playing MP3 files locally without internet connection.

## Features

✅ Play local MP3 files
✅ Playlist management
✅ Search and filter songs
✅ Shuffle and repeat modes
✅ Now Playing screen with album art
✅ Background playback
✅ Notification controls
✅ Recently played history
✅ Fast forward and rewind
✅ Volume control

## Requirements

- Android 8.0 (API 26) or higher
- Kotlin 1.9+
- Android Gradle Plugin 8.0+

## Installation

1. Clone the repository
2. Open the project in Android Studio
3. Build and run on an Android device or emulator

## Project Structure

```
app/
├── src/main/
│   ├── java/com/patricksynnah/musicplayer/
│   │   ├── ui/
│   │   ├── service/
│   │   ├── repository/
│   │   ├── viewmodel/
│   │   ├── adapter/
│   │   └── MainActivity.kt
│   ├── res/
│   │   ├── layout/
│   │   ├── drawable/
│   │   ├── values/
│   │   └── menu/
│   └── AndroidManifest.xml
├── build.gradle.kts
└── proguard-rules.pro
```

## Architecture

- **MVVM** Pattern with ViewModels
- **Repository Pattern** for data access
- **MediaPlayer** for audio playback
- **Service** for background playback
- **LiveData** for reactive UI updates

## Technologies Used

- Kotlin
- Android Jetpack (ViewModel, LiveData, Room)
- MediaPlayer API
- Material Design 3
- Coroutines

## Permissions

The app requires the following permissions:
- `READ_EXTERNAL_STORAGE` - To access audio files
- `FOREGROUND_SERVICE` - For background music playback

## License

MIT License
