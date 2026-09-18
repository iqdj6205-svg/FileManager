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
- `[x]` Selectable media bucket filters in phone media UI — `851cc83`.
- `[~]` Media preview visuals — `16b9a57` adds visual media-type preview cards and safer image empty state; real video thumbnail/playback preview still pending.
- `[x]` Phone image preview migrated to shared UI scaffold/cards — `eddb9ae`.
- `[x]` Phone media permission rationale copy — `d26aae7`.
- `[x]` Theme selector wired to `AppSettingsUseCase.setTheme` — `d890764`.
- `[x]` Human-readable storage root paths — `d890764`.
- `[x]` Auto-open added SAF tree — `eba5b2f`.
- `[x]` Phone settings migrated to shared UI scaffold/cards — `c8ac6a1`.
- `[x]` Phone media screens migrated to shared UI scaffold/cards — `6def875`.
- `[x]` Phone storage roots migrated to shared UI scaffold/cards — `e5c672e`.

### Wear

- `[x]` Migrate Wear media library screens to shared `WearRotaryList` — `6f8ad40`.
- `[x]` Migrate Wear playback controls to shared `WearRotaryList` — `6f8ad40`.
- `[x]` Improve Wear playback metadata/timeline display — `6f8ad40`.
- `[x]` Migrate Wear image preview to shared `WearRotaryList` and avoid empty URI rendering — `6f8ad40`.
- `[x]` Wear remote settings passed into embedded server factory/config — `78c4ace`.
- `[x]` Add Wear Data Layer `DATA_CHANGED` manifest filter symmetry — `de834c9`.
- `[~]` Real Wear rename text input — `8d2fce8` adds editable `BasicTextField` plus explicit save/presets; needs device validation.
- `[x]` Wear permission rationale explains media vs protected storage and ADB risk — `d26aae7`.
- `[~]` Remote server status/list/download consistency — `1ff1d86` publishes embedded start/stop session to shared status store; `b67ef45` exposes reachable local IP URL and blocks offline start; `2f3d5fb` enforces local-network start when local-only is enabled; `0e77b2e` shows active config in web manager; `pending` requires exact PIN for protected API routes; endpoint verification still pending.

## Full audit history

The full requirements, Wear, and Phone audit tracker existed before commit `7bb93ef`. This compact active tracker is being updated for the current safe-fix stream while the core audit is pending.
