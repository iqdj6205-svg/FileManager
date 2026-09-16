# FileManager for Wear OS

Full-featured file manager for Wear OS, designed for round watches and advanced users.

## Vision

Build a powerful, battery-aware file manager for Wear OS with a companion Android phone app.

Core goals:

- Round-watch optimized UI
- Gesture-first navigation
- Local file browsing
- File operations: open, share, rename, copy, move, delete, details
- Media gallery
- Audio/video playback foundation
- HTTP server for browser-based remote management
- Optional future FTP/WebDAV-style remote access
- Safe permission handling for media and documents
- Advanced ADB workflows for power users
- Optional Android phone companion app
- Battery and security conscious defaults

## Modules

```text
wear-app      Wear OS app
phone-app     Android phone companion
core-model    Shared models
core-files    File browsing, operations, analyzer
core-media    Media classification/library foundations
core-remote   Remote server/session/security foundations
core-ui       Shared UI helpers
```

## Current status

The repository contains an Android multi-module project skeleton with Wear OS and phone app foundations, product specs, security model, file operation architecture, remote access design, media foundations, diagnostics, and storage analyzer foundations.

## Build

Open in Android Studio and let Gradle sync. Then run:

```powershell
.\gradlew.bat build
```

or on macOS/Linux:

```bash
./gradlew build
```

See `docs/BUILD_AND_TEST.md` and `docs/NEXT_STEPS.md`.

## Documentation

- `docs/PRODUCT_SPEC.md`
- `docs/ARCHITECTURE.md`
- `docs/FEATURE_MATRIX.md`
- `docs/SECURITY_MODEL.md`
- `docs/REMOTE_ACCESS.md`
- `docs/MEDIA_PLAN.md`
- `docs/ADB_ADVANCED_MODE.md`
- `docs/STORAGE_ANALYZER.md`
- `docs/BUILD_AND_TEST.md`
