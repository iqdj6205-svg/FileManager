# Audit Tracker

This file is the persistent source of truth for external/local audit findings and stabilization progress.

Status legend:

- `[ ]` not started
- `[~]` partially addressed / needs verification
- `[x]` addressed in code
- `[hold]` postponed intentionally

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
