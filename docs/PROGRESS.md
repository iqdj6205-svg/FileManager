# Project Progress

## Current honest status

The project is **pre-alpha**.

## Stabilization updates

- Wear permission state is now read through `PermissionStateReader` at ViewModel startup/resume.
- Shared UI system foundations added for Wear and phone screens.
- Wear Permission, ADB guide and Remote dashboard screens now use shared Wear UI components.
- Wear storage root state is now part of app state.
- Wear storage roots can be selected from Home/Settings and open the selected practical folder.
- First Wear audit response committed: permission callbacks/readiness, RemoteSettings back target, and fake ServiceBackedRemoteController status were corrected.
- Wear navigation now has a real back stack for current screens instead of hardcoded one-off returns.
- Wear Home and Files migrated to shared rotary UI helpers.
- Wear rename action now opens an editable input screen instead of silently renaming to `copy-*`.
- First Phone audit response committed: remote settings security fields, URL validation, transfer empty state, clearer bridge status refresh, clipboard guidance, and phone emoji cleanup.
- Requirements audit has been preserved in `docs/AUDIT_TRACKER.md`.
- Phone-to-watch transfer now has editable target path, UI sending state, local streaming progress, and channel close in `finally`.
- Phone file actions now expose Share and Favorite actions; Share opens Android's share sheet, Favorite is marked as pending persistence instead of pretending to be complete.
- Phone media library now displays selectable media bucket filters, bucket names on rows, cleaner text markers, and richer preview metadata.
- Phone media preview now has visual media-type preview cards, disables player for unsupported media, and image preview avoids empty URI rendering.
- Phone settings now exposes a theme selector wired to persisted settings, and storage roots avoid showing raw `content://` URIs as user-facing paths.
- Adding a SAF folder on phone now selects and opens that folder immediately.
- Wear media library, image preview, and playback controls now use shared rotary UI helpers; playback shows timeline and metadata when available.
- Wear `RemoteServerService` now reads synced/local Wear remote settings before start and passes them into the embedded server factory, so port/PIN/upload/delete config reaches `SimpleHttpEngine`.
- Wear bridge manifest now includes a `DATA_CHANGED` filter alongside message handling for Data Layer symmetry.

## Current focus

1. Process Wear, Phone and requirements audit findings by priority.
2. Stabilize complete vertical flows instead of adding foundation-only code.
3. Use shared UI components instead of per-screen custom styling.
4. Make Wear standalone file browsing and remote status/download actually reliable.
5. Make phone companion remote/transfer flows useful instead of button-only shells.

## Next stabilization block

- Validate latest Wear rename input on device/build.
- Remote server status/list/download consistency.
- Permission rationale UX.
