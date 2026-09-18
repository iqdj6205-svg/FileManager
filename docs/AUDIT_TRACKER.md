# Audit Tracker

This file is the persistent source of truth for external/local audit findings and stabilization progress.

Status legend:

- `[ ]` not started
- `[~]` partially addressed / needs verification
- `[x]` addressed in code
- `[hold]` postponed intentionally

## Active independent fixes while core audit is pending

### Phone

- `[x]` Editable target path for phone-to-watch file transfer — `cea0b06`.
- `[x]` Local transfer progress during channel copy — `cea0b06`.
- `[x]` Close Wear channel in `finally` after transfer — `cea0b06`.
- `[x]` Share action for phone files — `7bb93ef`.
- `[~]` Favorite action — UI exposed with honest pending-persistence message in `7bb93ef`; persistence still pending.
- `[~]` Media buckets — summary chips and row bucket names in `e2d2f47`; selectable filters still pending.
- `[~]` Media preview — metadata improved in `e2d2f47`; visual preview still pending.
- `[x]` Theme selector wired to `AppSettingsUseCase.setTheme` — `d890764`.
- `[x]` Human-readable storage root paths — `d890764`.
- `[x]` Auto-open added SAF tree — `eba5b2f`.

### Wear

- `[x]` Migrate Wear media library screens to shared `WearRotaryList` — `6f8ad40`.
- `[x]` Migrate Wear playback controls to shared `WearRotaryList` — `6f8ad40`.
- `[x]` Improve Wear playback metadata/timeline display — `6f8ad40`.
- `[x]` Migrate Wear image preview to shared `WearRotaryList` and avoid empty URI rendering — `6f8ad40`.
- `[x]` Wear remote settings passed into embedded server factory/config — `78c4ace`.
- `[x]` Add Wear Data Layer `DATA_CHANGED` manifest filter symmetry — `pending`.
- `[ ]` Real Wear rename text input.
- `[ ]` Remote server status/list/download consistency.
- `[ ]` Permission rationale/permanently-denied UX.

## Full audit history

The full requirements, Wear, and Phone audit tracker existed before commit `7bb93ef`. This compact active tracker is being updated for the current safe-fix stream while the core audit is pending.
