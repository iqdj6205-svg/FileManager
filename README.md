# FileManager for Wear OS

Full-featured file manager for Wear OS, designed for round watches and advanced users.

## Vision

Build a powerful, battery-aware file manager for Wear OS with:

- Round-watch optimized UI
- Gesture-first navigation
- Local file browsing
- Media gallery
- Audio/video playback
- HTTP server for browser-based remote management
- Optional FTP/WebDAV-style remote access
- Safe permission handling for media and documents
- Advanced ADB workflows for full storage access where Android allows it
- Optional Android phone companion app

## Main modules

### Wear OS app

- File browser
- Favorites and recent files
- Search
- Copy, move, rename, delete
- Archive support
- Storage overview
- Permission onboarding
- Battery-aware background behavior

### Media tools

- Image gallery
- Video preview/player
- Audio player
- Metadata display
- Share/open-with actions

### Remote access

- Local HTTP server
- QR code connection from phone/desktop
- Upload/download files
- Basic authentication / session PIN
- Network and battery safety limits

### Advanced mode

- ADB instructions
- Developer warnings
- Extended path access where supported
- Diagnostics and logs

### Phone companion

- Setup assistant
- File transfer
- Remote server control
- Storage dashboard
- Watch connection status

## Tech direction

- Kotlin
- Jetpack Compose for Wear OS
- Material 3 / Wear Material components
- Coroutines + Flow
- Media3 for playback
- Android Storage Access Framework where appropriate
- Lightweight embedded HTTP server

## Status

Project created. Architecture and implementation plan are in progress.
