# Build and Test Guide

## Local build

Open the repository in Android Studio, let Gradle sync, then run:

```powershell
.\gradlew build
```

On macOS/Linux:

```bash
./gradlew build
```

## Wear OS testing

Recommended testing order:

1. Android Studio Gradle sync
2. Wear OS emulator round device
3. Real watch installation
4. Permission flow check
5. Battery behavior check
6. Remote server check only on trusted local network

## First manual test checklist

- App launches on round Wear OS emulator
- Main menu fits on circular screen
- Files screen opens
- Media screen opens
- Remote screen opens
- Settings screen opens
- Advanced screen opens
- Back button returns to home

## Known early limitations

This is an early skeleton. Some screens are placeholders and will be replaced with real file operations, permission handling, and remote server implementation.
