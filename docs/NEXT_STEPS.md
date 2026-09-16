# Next Steps

## Immediate code work

1. Validate Gradle configuration in Android Studio.
2. Replace placeholder remote server with a real embedded HTTP implementation.
3. Add actual runtime permission requests.
4. Add file action UI: rename, copy, move, delete, details.
5. Add persistent settings.
6. Add real MediaStore integration.
7. Add phone-to-watch transfer protocol.

## Manual checkpoint for Serg

When ready to test locally:

```powershell
git clone https://github.com/iqdj6205-svg/FileManager.git
cd FileManager
.\gradlew.bat build
```

If Android Studio asks to generate or update Gradle wrapper files, allow it.

## Known limitations before first local build

- Gradle wrapper is a lightweight launcher and expects Gradle installed or Android Studio to regenerate the official wrapper.
- Exact dependency compatibility should be validated by Gradle sync.
- Remote HTTP server currently has facade and web UI assets, not full socket routing.
- File access depends on Wear OS storage permissions and device policy.
