# Audit Tracker

This file is the persistent source of truth for external/local audit findings and stabilization progress.

Status legend:

- `[ ]` not started
- `[~]` partially addressed / needs verification
- `[x]` addressed in code
- `[hold]` postponed intentionally

## Active independent phone fixes while core audit is pending

- `[x]` Add editable target path for phone-to-watch file transfer.
  - Commit: `cea0b06`.
- `[x]` Add local transfer progress during `WearChannelTransferClient` copy.
  - Commit: `cea0b06`.
- `[x]` Close Wear channel in `finally` after transfer.
  - Commit: `cea0b06`.
- `[x]` Add Share action for phone files.
  - Commit: `7bb93ef`.
- `[~]` Add real Favorite action or remove the placeholder.
  - Commit: `7bb93ef` exposes the UI action with an honest pending-persistence message.
  - Remaining: persistent FavoritesRepository/DataStore/Room integration.
- `[~]` Show media buckets / album filters in phone media UI.
  - Commit: `e2d2f47` shows bucket chips and row bucket names.
  - Remaining: make chips selectable filters instead of read-only summary chips.
- `[~]` Improve media preview with actual image/video preview.
  - Commit: `e2d2f47` improves metadata details only.
  - Remaining: visual image/video preview.
- `[x]` Add theme selector wired to `AppSettingsUseCase.setTheme`.
  - Commit: `pending`.
- `[x]` Make storage root display paths human-readable.
  - Commit: `pending`.
- `[ ]` After adding SAF tree, select/open it automatically.
- `[ ]` Unify SAF/local operation progress sink.
- `[ ]` Improve permission rationale/permanently-denied UX.

## Full audit history

The full requirements, Wear, and Phone audit tracker existed before commit `7bb93ef`. This compact active tracker is being updated for the current safe-fix stream while the core audit is pending.
