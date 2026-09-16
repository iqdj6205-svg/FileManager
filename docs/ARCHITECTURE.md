# Architecture

## Goals

The app should feel native on small round Wear OS screens while still offering power-user file management features.

## Proposed structure

```text
FileManager/
  wear-app/          Wear OS application
  phone-app/         Optional Android companion
  core/              Shared domain logic
  data/              File system, media, settings, remote server data layer
  docs/              Planning and technical notes
```

## Core principles

1. **Watch-first UX**
   - Large touch targets
   - Short labels
   - Minimal typing
   - Gesture and rotary input support
   - Round-screen safe layout

2. **Safe storage access**
   - Request only required permissions
   - Explain Android/Wear OS storage limitations clearly
   - Use standard Android APIs where possible
   - Offer ADB guidance only as an advanced option

3. **Battery awareness**
   - No permanent background scanning
   - Stop remote servers automatically
   - Limit thumbnail generation
   - Use foreground services only when necessary

4. **Security by default**
   - Remote access off by default
   - PIN/session pairing
   - Local-network-only by default
   - Clear warnings before enabling servers

## Main layers

### UI layer

Jetpack Compose for Wear OS screens:

- Home screen
- File list
- File actions sheet
- Media viewer
- Remote access screen
- Settings
- Advanced mode

### Domain layer

Use cases:

- BrowseDirectory
- CopyFile
- MoveFile
- DeleteFile
- RenameFile
- ScanMedia
- StartRemoteServer
- StopRemoteServer
- GeneratePairingCode

### Data layer

Repositories:

- FileRepository
- MediaRepository
- SettingsRepository
- RemoteServerRepository
- BatteryPolicyRepository

## Remote access design

The first remote access target should be HTTP because it is easiest to use from a phone or desktop browser. FTP/WebDAV can be evaluated later.

Minimum HTTP features:

- Browse directories
- Download file
- Upload file
- Delete file
- Rename file
- Create folder
- Show storage status

## Open questions

- Minimum Wear OS version?
- Kotlin-only or Kotlin Multiplatform for shared code?
- Should FTP be native or deferred in favor of HTTP/WebDAV?
- Should the first version include phone companion or launch watch-only first?
