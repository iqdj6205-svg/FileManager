# Product Specification — FileManager for Wear OS

## 1. Product idea

FileManager is a full-featured file manager for Wear OS and Android phones. The Wear OS app must be fully standalone and useful without a phone. The Android phone app is optional, but when installed it acts both as a companion/helper for the watch and as a full-featured Android file manager.

The goal is to make file management on a watch practical, safe, fast, and powerful, while respecting Android/Wear OS storage, security, and battery limitations.

The project is inspired by mature Android file managers such as Solid Explorer, MiXplorer, Total Commander, X-plore, Amaze, and Material Files, but adapted for tiny round Wear OS screens.

## 2. Core principles

### Watch-first standalone product

The Wear OS app must not depend on the phone companion for core behavior.

The watch app must independently support:

- browsing files;
- requesting permissions;
- file operations;
- media browsing;
- remote server start/stop;
- server address/PIN display;
- settings;
- advanced ADB guidance.

### Optional but powerful phone app

The phone app is not required, but should be useful when installed.

It should provide:

- full Android phone file manager functionality;
- watch companion controls;
- easier typing and setup;
- watch server control;
- file transfer between phone and watch;
- storage/battery/status dashboard.

### Watch-first, not phone-shrunk

The Wear OS app must not feel like a phone app squeezed onto a watch.

Requirements:

- Round-screen safe layouts
- Large touch targets
- Short labels
- Minimal typing
- Gesture-based navigation
- Rotary input support where available
- Fast one-handed actions
- Optional haptic feedback
- Dark theme first

### Safe power-user functionality

The app should support advanced users but avoid unsafe promises.

Requirements:

- Use official Android permissions where possible
- Explain storage restrictions clearly
- Provide ADB workflows as advanced documentation
- Avoid pretending that normal apps can access protected system folders without user/root/ADB action
- Never run remote access servers silently

### Battery-aware architecture

Wear OS devices have limited battery and thermal capacity.

Requirements:

- No always-on file scanning by default
- Remote servers auto-stop by timeout, battery level, and screen/session state
- Thumbnail generation should be lazy and cached
- Background work should be minimal
- Expensive operations need progress and cancellation

### Security by default

Remote access is powerful and risky.

Requirements:

- Remote server disabled by default
- PIN/session authentication
- Local network only by default
- Optional one-time pairing QR code
- Clear warnings before enabling upload/delete features
- Auto-stop server on timeout
- No plain unauthenticated write access

## 3. Target platforms

### Wear OS app

Primary standalone application.

Expected stack:

- Kotlin
- Jetpack Compose for Wear OS
- Wear Material components
- Coroutines and Flow
- AndroidX Lifecycle / ViewModel
- Media3 for audio/video playback where practical
- Storage Access Framework where applicable
- Lightweight embedded HTTP server

### Android phone app

Optional companion and full phone file manager.

Main purposes:

- Easier setup
- File transfer between phone and watch
- Remote server control
- Storage and battery dashboard
- Larger-screen file management UI
- Full Android file manager features using shared core modules

## 4. Main feature set

See `docs/PRODUCT_STRATEGY.md`, `docs/FEATURE_MATRIX.md`, and implementation docs for detailed roadmap.
