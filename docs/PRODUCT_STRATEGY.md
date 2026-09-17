# Product Strategy

## Core product principle

FileManager is not a phone-dependent watch utility. The Wear OS application must be a complete, standalone, fully usable file manager.

The Android phone application is optional. It is a companion and helper, but it should also grow into a full-featured Android file manager with the same core capabilities.

## Wear OS app

The watch app must work independently without a phone app installed.

Required standalone capabilities:

- Browse accessible watch storage
- Request and explain storage/media permissions
- Open, rename, copy, move, delete, and inspect files
- Show favorites and recent files
- Search and sort files
- Display media lists and previews where practical
- Start/stop remote HTTP server directly from the watch
- Show server address, PIN, network status, and safety settings on the watch
- Configure battery and security options directly on the watch
- Provide advanced ADB guidance directly on the watch

The watch app should never require the phone companion for normal file management.

## Phone app

The phone app is optional but powerful.

It should have two roles:

1. **Companion/helper for the watch**
   - Send settings to the watch
   - Start/stop the watch server
   - Receive watch server address/PIN/status
   - Transfer files to/from the watch
   - Help with typing paths, filenames, and advanced settings

2. **Full Android file manager**
   - Browse phone storage
   - File operations
   - Media gallery/player
   - HTTP/WebDAV/FTP features where appropriate
   - Storage analyzer
   - Advanced tools

## Shared architecture

Most business logic should be shared between watch and phone:

```text
core-model       shared models
core-files       file operations and storage analysis
core-media       media classification and MediaStore support
core-remote      HTTP server/client foundations
core-ui          shared UI helpers/design tokens
core-wear-bridge phone-watch communication protocol
wear-app         standalone Wear OS app
phone-app        full phone file manager + optional companion
```

## Package/application ID strategy

The apps may use the same base product identity while keeping separate module namespaces:

- Wear namespace: `com.sere.filemanager.wear`
- Phone namespace: `com.sere.filemanager.phone`

For Data Layer communication, both apps must be signed with the same certificate and declare compatible Wear capabilities. They do not need to make the watch dependent on the phone.

## Wear Data Layer use cases

Optional phone-watch bridge features:

- `START_WATCH_SERVER`
- `STOP_WATCH_SERVER`
- `GET_WATCH_SERVER_STATUS`
- `SET_WATCH_REMOTE_CONFIG`
- `GET_WATCH_STORAGE_STATUS`
- `SEND_FILE_TO_WATCH`
- `REQUEST_FILE_FROM_WATCH`
- `SYNC_SETTINGS`

These are convenience features, not requirements for using the watch app.
