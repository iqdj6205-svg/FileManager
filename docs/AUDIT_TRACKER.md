# Audit Tracker

This file is the persistent source of truth for external/local audit findings and stabilization progress.

Status legend:

- `[ ]` not started
- `[~]` partially addressed / needs verification
- `[x]` addressed in code
- `[hold]` postponed intentionally

## Requirements/documentation audit

Source: user-provided audit of 55 Markdown requirement/status documents (`README.md`, `ROADMAP.md`, and `docs/*.md`).

### Product / strategy requirements

- `[x]` Keep project status honest as pre-alpha instead of percent-based readiness.
  - Commit: `51355e1`.
- `[ ]` Maintain watch-first standalone product rule: Wear app must remain useful without phone.
- `[ ]` Ensure phone app is both optional companion and full phone file manager.
- `[ ]` Keep shared `core-*` architecture and avoid isolated unfinished skeletons.
- `[ ]` Confirm package/certificate requirements for Wear Data Layer release setup.

### Wear file manager requirements

- `[~]` Accessible storage root selector: shared storage, MediaStore, ADB advanced.
  - Commits: `1a56d16`, `fab7b50`.
  - Remaining: persistence, permission filtering, remote/transfer start-path integration.
- `[x]` Home contains Files/Media/Remote/Settings/Advanced routes.
  - Commit: `fab7b50`.
- `[~]` Round-safe file list with rotary scrolling and compact path.
  - Commit: `fab7b50`.
  - Remaining: proper icons, breadcrumbs, consistent all-screen migration.
- `[ ]` Search and sort/filter for files.
- `[ ]` Persistent favorites and recent files.
- `[ ]` Show hidden files toggle applied to browser listing.
- `[~]` Permission onboarding / Grant access hub.
  - Commits: `bb6a88b`, `1826f7d`.
  - Remaining: rationale/permanently-denied UX, media-vs-full-FS explanation polish.
- `[ ]` Storage analyzer on Wear or explicit decision to phone-first analyzer.

### Wear file operations

- `[~]` Open/rename/copy/move/delete/details/create-folder basics.
  - Remaining: real open/share/favorite, details metadata, overwrite confirmation, real rename input.
- `[~]` Copy/move clipboard + paste.
  - Remaining: progress/cancellation for large operations, destination confirmation.
- `[~]` Delete confirmation.
  - Remaining: softer delete/undo or stronger target-path wording.
- `[ ]` Share action.
- `[ ]` Favorite action with persistence.
- `[ ]` Duplicate helper with keep-both name policy exposed or remove duplicate dead path.
- `[ ]` Conflict resolver UI: keep-both/replace/skip/fail.
- `[ ]` Protected path/local traversal guards beyond remote guards.

### Media requirements

- `[~]` MediaStore-backed images/audio/video library.
  - Remaining: permission-aware load, bucket filters, pagination beyond 300 or explicit limit UI.
- `[~]` Image preview via Coil on Wear/phone.
  - Remaining: placeholder/error state, pinch/rotary zoom, share/details.
- `[~]` Audio/video playback foundation via Media3.
  - Remaining: progress seek bar, metadata rendering, foreground media service, headset/Bluetooth, no-op controller messaging.
- `[ ]` Lazy thumbnail cache with battery thresholds.
- `[ ]` Video preview/open-with and/or stable Media3 playback UX.
- `[ ]` MediaSession notification/lock-screen/compact Wear behavior completed.

### Remote HTTP requirements

- `[~]` HTTP server as first protocol, FTP/WebDAV deferred.
  - Remaining: production-grade engine validation and web manager completion.
- `[~]` Off-by-default explicit start/stop.
  - Remaining: status consistency, notification permission explanation, failure UI.
- `[~]` Show URL/PIN/network/battery on Wear UI.
  - Commits: `1826f7d`, `8f46985` partially.
  - Remaining: real status source end-to-end and remote settings config application.
- `[~]` PIN/local-only/upload/delete security flags.
  - Commit: `8f46985` exposes phone settings.
  - Remaining: apply Wear settings to `RemoteServerService` config and verify enforcement.
- `[ ]` QR pairing / one-time session flow.
- `[ ]` Auto-stop by timeout + low battery + session/screen policy verified.
- `[~]` API endpoints: `/`, `/api/status`, `/api/list`, `/api/download`, `/api/upload`, `/api/rename`, `/api/delete`, `/api/mkdir`, `/api/audit`, `/api/audit/export`.
  - Remaining: route policy consistency, upload multipart, audit wiring, HTTP status codes.
- `[~]` Binary download streaming.
  - Remaining: verify no RAM loading and path guards in actual engine.
- `[ ]` Multipart upload parser and progress.
- `[ ]` Web manager: breadcrumbs, list/download/mkdir/upload/rename/delete, health warnings, toasts.
- `[ ]` Rate limiting 429 per IP / destructive route.
- `[ ]` Persistent audit log + export with client IP.
- `[ ]` Binding/local-only interface checks.

### Wear bridge requirements

- `[~]` MessageClient commands Start/Stop/GetStatus/SyncSettings/GetStorageStatus/Send/Request file.
  - Remaining: complete GetStorageStatus/Request file UX and error surfacing.
- `[~]` DataClient snapshots for remote status/settings/transfer.
  - Remaining: symmetry filters, await/onFailure handling, reactive UI proof.
- `[~]` ChannelClient phone-to-watch file transfer.
  - Remaining: editable destination, progress, close channel in finally, size limit, path hardening.
- `[ ]` Reverse watch-to-phone file transfer.
- `[ ]` Pair/Ping flow and dashboard storage/battery.
- `[ ]` Move received file from private to public storage when permissions allow.

### Settings / persistence requirements

- `[~]` DataStore-backed phone settings.
  - Remaining: apply theme/show-hidden/remote/battery/haptics everywhere.
- `[ ]` DataStore-backed Wear settings.
- `[ ]` Sync settings via Data Layer and apply them to real Wear services.
- `[ ]` Confirm delete setting.
- `[ ]` Theme selector UI and actual theme application.
- `[ ]` Haptics setting wired to interactions.
- `[ ]` Favorites/recent persistence via DataStore/Room.

### UX / battery / security requirements

- `[~]` Round-safe layouts, large targets, short labels, vertical scrolling.
  - Remaining: migrate all Wear screens, replace placeholders, a11y labels.
- `[~]` Gesture/rotary support.
  - Remaining: media/player/settings/secondary screens consistency.
- `[ ]` Battery-aware behavior: no background scans, lazy thumbnails, cancellable scans, server auto-stop.
- `[ ]` Explicit warnings before upload/delete and advanced ADB actions.
- `[ ]` Diagnostics/export logs.
- `[ ]` Localization / string resources.
- `[ ]` Play Store readiness pass.

## Wear app audit

### P0 / critical

- `[x]` Fix `Grant files` callback using media request path by accident.
  - Commit: `1826f7d`.
- `[x]` Rework permission readiness so file access is not blocked by duplicated `mediaImages` logic.
  - Commit: `1826f7d`.
- `[x]` Make Permission Hub scrollable on small round screens.
  - Commit: `1826f7d`.
- `[x]` Stop returning fake remote URL/PIN from `ServiceBackedRemoteController`.
  - Commit: `1826f7d`.
- `[x]` Fix `RemoteSettings` back destination when opened from Remote.
  - Commits: `1826f7d`, `fab7b50`.
- `[x]` Replace hardcoded one-screen back mapping with a small back stack.
  - Commit: `fab7b50`.
- `[x]` Add operation progress UI for running file operations.
  - Commit: `fab7b50`.
- `[~]` Rename should not silently rename to `copy-*`.
  - Commit: `fab7b50` added explicit temporary rename presets.
  - Remaining: real keyboard/voice/RemoteInput rename.

### P1 / functionality

- `[x]` Wire practical storage roots into Wear app state and browser flow.
  - Commit: `1a56d16`.
- `[x]` Expose Storage roots from Home/Settings.
  - Commit: `1a56d16`.
- `[x]` Migrate Wear Home and Files to shared `WearRotaryList`.
  - Commit: `fab7b50`.
- `[ ]` Migrate Wear media lists/player/image screens fully to shared Wear UI.
- `[ ]` Show media/session metadata in playback UI.
- `[ ]` Replace no-op playback state when `playbackController == null` with clear UI messaging.
- `[ ]` Improve file details for very long paths and richer metadata.
- `[ ]` Connect or remove `copySelected()` / `duplicateSelected()` dead paths.
- `[ ]` Replace in-memory favorites with persistent favorites or remove Favorite until real.
- `[ ]` Make `batterySaverEnabled` and `advancedModeEnabled` affect real behavior.
- `[ ]` Persist Wear remote/settings instead of process-memory only.
- `[ ]` Wire Wear remote settings into real `RemoteServerService` config.
- `[ ]` Harden Wear channel receive path canonicalization and size limits.
- `[ ]` Add Data Layer `DATA_CHANGED` filter symmetry on Wear if needed.

### P2 / cleanup and UX

- `[x]` Replace Wear file-list emoji markers with text markers to avoid missing glyphs.
  - Commit: `fab7b50`.
- `[ ]` Replace text markers with proper Material icons later.
- `[ ]` Remove duplicate/dead screens after verifying references.
- `[ ]` Replace placeholder Theme/Haptics/Diagnostics settings.
- `[ ]` Move ADB commands to safer generated strings if package ID changes.
- `[ ]` Add rationale/permanently-denied permission UX.

## Phone app audit

### P0 / critical

- `[x]` Expose `requirePin` in Remote Settings UI.
  - Commit: `8f46985`.
- `[x]` Expose `localNetworkOnly` in Remote Settings UI.
  - Commit: `8f46985`.
- `[x]` Add editable remote `port` with validation.
  - Commit: `8f46985`.
- `[x]` Add editable `autoStopMinutes` with validation.
  - Commit: `8f46985`.
- `[x]` Make Sync-to-watch send user-editable remote settings rather than hidden defaults.
  - Commit: `8f46985`.
- `[x]` Validate Remote URL before browser open.
  - Commit: `8f46985`.
- `[x]` Rename Remote Manager action honestly to `Open in browser` until real in-app manager exists.
  - Commit: `8f46985`.
- `[x]` Change `Read latest status` into a real watch status request.
  - Commit: `8f46985`.
- `[x]` Make transfer status card visible even before first transfer.
  - Commit: `8f46985`.

### P1 / functionality

- `[x]` Clarify clipboard copy/move two-step UX.
  - Commit: `8f46985`.
- `[x]` Add empty-folder state to Phone Files.
  - Commit: `8f46985`.
- `[x]` Remove emoji dependency from Phone Home/File rows.
  - Commit: `8f46985`.
- `[ ]` Add editable target path for phone-to-watch file transfer.
- `[ ]` Add real transfer progress during `WearChannelTransferClient` copy.
- `[ ]` Close Wear channel in `finally` after transfer.
- `[ ]` Add Share action for phone files.
- `[ ]` Add real Favorite action or remove the placeholder.
- `[ ]` Show media buckets / album filters in phone media UI.
- `[ ]` Improve media preview with actual image/video preview.
- `[ ]` Add theme selector wired to `AppSettingsUseCase.setTheme`.
- `[ ]` Make storage root display paths human-readable.
- `[ ]` After adding SAF tree, select/open it automatically.
- `[ ]` Unify SAF/local operation progress sink.
- `[ ]` Improve permission rationale/permanently-denied UX.

### P2 / cleanup and UX

- `[ ]` Move duplicate settings rows/toggles into shared `core-ui` components.
- `[ ]` Remove unused `PhoneCreateFolderState` if still dead.
- `[ ]` Remove unused `PhoneFileAction` enum if still dead.
- `[ ]` Remove unused `TransferQueue` if still dead.
- `[ ]` Decide whether `CompanionState` should be wired or removed.
- `[ ]` Replace simple mutable screen state with real Navigation when stabilization allows.
- `[ ]` Make Remote Manager an actual in-app remote file manager with PIN/list/download/upload/delete.

## Incoming reports

Paste additional audit reports into chat. After each one, add a new section here before starting fixes, then mark items as `[x]` / `[~]` with commit IDs as fixes land.
