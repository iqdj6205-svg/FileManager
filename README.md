# FileManager for Wear OS

FileManager is an Android/Wear OS file manager project aimed at building a powerful standalone Wear OS file manager plus an optional full-featured Android phone companion.

## Honest current status

This project is currently a **pre-alpha prototype**.

It contains a multi-module Android project, Wear app, phone app, remote server foundations, media foundations, SAF/file-operation foundations and phone-watch bridge foundations. However, many features are still incomplete, fragile, or only partially connected.

Do **not** treat the current app as beta or alpha quality yet.

See:

- `docs/REAL_STATUS.md`
- `docs/ALPHA_STABILIZATION_PLAN.md`
- `docs/SMOKE_TEST_CHECKLIST.md`

## Vision

Build a powerful, battery-aware file manager for Wear OS with an optional Android phone companion.

Core goals:

- Standalone Wear OS file manager.
- Round-watch optimized UI.
- Gesture and rotary-friendly navigation.
- Local file browsing and practical storage roots.
- File operations: details, rename, copy, move, delete.
- Media gallery and playback foundations.
- HTTP server for browser-based remote management.
- Optional future FTP/WebDAV-style remote access.
- Safe permission handling for media and documents.
- Advanced ADB workflows for power users.
- Optional Android phone companion with full file-manager functionality.
- Battery and security conscious defaults.

## Modules

```text
wear-app           Wear OS app
phone-app          Android phone app / optional companion
core-model         Shared models
core-files         File browsing, operations, analyzer and SAF foundations
core-media         Media classification/library/playback foundations
core-remote        Remote server/session/security foundations
core-ui            Shared UI helpers
core-wear-bridge   Wear Data Layer command/status/transfer foundations
```

## Build

Open in Android Studio and let Gradle sync. Then run:

```powershell
.\gradlew.bat build
```

or on macOS/Linux:

```bash
./gradlew build
```

## Stabilization direction

The next phase is not broad feature expansion. The priority is stabilizing complete vertical flows:

1. Wear permissions and storage roots.
2. Wear file browsing/details/rename/delete/copy/move.
3. Remote server start/stop/status/list/download.
4. Phone file browser and SAF operations.
5. Phone-watch status and one-file transfer.
