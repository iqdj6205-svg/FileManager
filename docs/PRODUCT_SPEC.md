# Product Specification — FileManager for Wear OS

## 1. Product idea

FileManager is a full-featured file manager for Wear OS and Android phones. The goal is to make file management on a watch practical, safe, fast, and powerful, while respecting Android/Wear OS storage, security, and battery limitations.

The project is inspired by mature Android file managers such as Solid Explorer, MiXplorer, Total Commander, X-plore, Amaze, and Material Files, but adapted for tiny round Wear OS screens.

## 2. Core principles

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

Primary application.

Expected stack:

- Kotlin
- Jetpack Compose for Wear OS
- Wear Material components
- Coroutines and Flow
- AndroidX Lifecycle / ViewModel
- Media3 for audio/video playback where practical
- Storage Access Framework where applicable
- Lightweight embedded HTTP server

### Android phone companion

Optional but planned.

Main purposes:

- Easier setup
- File transfer between phone and watch
- Remote server control
- Storage and battery dashboard
- Larger-screen management UI

## 4. Main feature set

## 4.1 File browser

### MVP features

- Browse accessible folders
- Show files and directories
- Navigate forward/back
- Compact path display
- Sort by name/date/size/type
- Search inside accessible locations
- Recent files
- Favorite folders
- File details screen

### File operations

- Open file
- Share file
- Rename
- Copy
- Move
- Delete
- Create folder
- Multi-select mode
- Progress UI for long operations
- Cancel long operations

### Advanced file operations

- Archive preview
- ZIP create/extract
- Batch rename
- Checksums
- Duplicate finder
- Large file finder
- Storage analyzer

## 4.2 Round watch UI

### Navigation model

Recommended navigation:

- Home dashboard
- File list
- Action sheet
- Details screen
- Remote access screen
- Settings
- Advanced mode

### Gestures

Possible gestures:

- Tap: open/select
- Long press: context actions
- Swipe right: back/up
- Swipe left: quick actions
- Rotary input: scroll list
- Double tap optional: favorite or preview

### UI rules

- Main actions must be reachable in 1–2 taps
- Avoid dense tables
- Prefer cards/chips/action sheets
- Avoid long filenames taking over the whole display
- Use marquee or detail screen for long paths

## 4.3 Media gallery

### Images

- Grid/list gallery optimized for round screen
- Fullscreen image viewer
- Zoom/pan if practical
- Basic metadata
- Share/open/delete actions
- Lazy thumbnails

### Audio

- Audio file list
- Playback controls
- Background playback if allowed and useful
- Metadata display
- Progress slider adapted for watch

### Video

- Preview or external open action initially
- Optional Media3 playback
- Battery warning for long playback

## 4.4 Remote management

### HTTP server — first priority

HTTP is the most practical first remote protocol because any phone/PC browser can use it.

Features:

- Start/stop server from watch
- Show connection URL
- Show QR code
- PIN/session auth
- Browse folders
- Download files
- Upload files
- Rename/delete/create folder if enabled
- Show storage usage
- Auto-stop timer

### FTP/WebDAV — later priority

FTP or WebDAV can be added later for compatibility with desktop tools.

Recommendation:

- Implement HTTP first
- Evaluate WebDAV before FTP because WebDAV fits modern file-management workflows better
- Keep FTP optional due to security concerns

## 4.5 Advanced access / ADB mode

Normal Android apps cannot freely access all protected storage. Advanced mode should be honest and safe.

Features:

- Explain storage limitations
- Provide ADB command guide
- Provide developer-mode checklist
- Provide import/export workflows
- Provide troubleshooting logs
- Optional root notes, clearly unsupported by default

The app should not claim full system memory access for all users.

## 4.6 Phone companion

### Companion MVP

- Pair with watch
- Send files to watch
- Receive files from watch
- Start/stop watch HTTP server
- Show watch storage and battery status
- Open web manager quickly

### Later companion features

- Backup selected watch folders
- Sync favorites
- Manage advanced settings
- Install helper configuration

## 5. App architecture

## 5.1 Repository modules

Recommended Gradle modules:

```text
FileManager/
  settings.gradle.kts
  build.gradle.kts
  gradle.properties
  wear-app/
  phone-app/
  core-model/
  core-files/
  core-media/
  core-remote/
  core-ui/
  docs/
```

### wear-app

Wear OS UI and app entry point.

### phone-app

Android phone companion UI.

### core-model

Shared models:

- FileItem
- FilePath
- StorageVolume
- OperationProgress
- RemoteSession
- BatteryPolicy

### core-files

File access and operations.

### core-media

Media indexing, metadata, thumbnails, playback helpers.

### core-remote

HTTP server, routing, auth/session logic.

### core-ui

Shared design tokens and UI helpers where useful.

## 6. Important technical constraints

## 6.1 Storage access

Android storage access is restricted by design. Different APIs provide different visibility:

- Media permissions for media files
- Storage Access Framework for user-selected documents/folders
- App-specific directories
- ADB for advanced workflows
- Root only for truly unrestricted system-level access

The product must be built around these constraints.

## 6.2 Wear OS limitations

Consider:

- Small screen
- Round display cutoffs
- Battery constraints
- Limited RAM/storage
- Background execution restrictions
- Network availability differences
- Input limitations

## 6.3 Remote server constraints

Running a server on a watch is expensive and potentially risky.

Rules:

- Use foreground indication if required
- Stop automatically
- Require explicit user action
- Avoid running when battery is low
- Avoid indexing whole storage while server starts

## 7. MVP definition

First working target:

- Wear OS app starts
- Home screen works
- File browser shows accessible demo/local/app storage
- Basic file operations implemented for accessible paths
- Settings screen exists
- Permissions screen exists
- Remote HTTP server can start and show status
- Phone companion skeleton exists
- Project builds in Android Studio

## 8. Future premium/power features

- Dual-pane mode on phone
- Cloud storage integrations
- SMB/SFTP/WebDAV client
- WebDAV server
- Plugin architecture
- File encryption vault
- Watch-to-phone automatic backup
- LAN discovery
- File preview system
- Task queue
- Theme editor

## 9. Development plan

### Step A — Project foundation

- Create real Android Gradle project
- Add Wear OS module
- Add phone module
- Add shared modules
- Add package structure
- Add basic screens

### Step B — File model and browser

- FileItem model
- FileRepository interface
- LocalFileRepository implementation
- Browser ViewModel
- File list UI

### Step C — Permissions and settings

- Permission screen
- Settings store
- Advanced mode flag
- Battery policy settings

### Step D — Remote HTTP prototype

- Server state model
- Start/stop server use cases
- Minimal HTTP server
- QR/pairing screen placeholder

### Step E — Media foundation

- Media item model
- Gallery screen
- Player screen skeleton

### Step F — Phone companion

- Phone app home screen
- Pairing placeholder
- Transfer placeholder

### Step G — Build verification

- Add GitHub Actions build workflow
- Fix compile errors
- Prepare local test instructions

## 10. Acceptance criteria for first complete code pass

- Repository contains a compilable Android project structure
- Wear OS app has navigable screens
- Phone app has companion skeleton
- Shared modules compile
- README explains how to build
- GitHub Actions workflow exists
- No dangerous remote access defaults
- Advanced storage limitations are documented
