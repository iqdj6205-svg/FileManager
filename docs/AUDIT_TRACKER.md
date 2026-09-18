# Audit Tracker

This file is the persistent source of truth for external/local audit findings and stabilization progress.

Status legend:

- `[ ]` not started
- `[~]` partially addressed / needs verification
- `[x]` addressed in code
- `[hold]` postponed intentionally

## Phone app audit

### P1 / functionality

- `[x]` Add editable target path for phone-to-watch file transfer.
  - Commit: `cea0b06`.
- `[x]` Add local transfer progress during `WearChannelTransferClient` copy.
  - Commit: `cea0b06`.
- `[x]` Close Wear channel in `finally` after transfer.
  - Commit: `cea0b06`.
- `[x]` Add Share action for phone files.
  - Commit: `pending`.
- `[~]` Add real Favorite action or remove the placeholder.
  - Commit: `pending` exposes the UI action with an honest pending-persistence message.
  - Remaining: persistent FavoritesRepository/DataStore/Room integration.
- `[ ]` Show media buckets / album filters in phone media UI.
- `[ ]` Improve media preview with actual image/video preview.
- `[ ]` Add theme selector wired to `AppSettingsUseCase.setTheme`.
- `[ ]` Make storage root display paths human-readable.
- `[ ]` After adding SAF tree, select/open it automatically.
- `[ ]` Unify SAF/local operation progress sink.
- `[ ]` Improve permission rationale/permanently-denied UX.

## Remaining tracker

Full tracker was previously captured in `docs/AUDIT_TRACKER.md`. This compact section keeps the currently active independent phone block in sync while core audit is pending. Restore/expand from Git history if a full audit view is needed.
