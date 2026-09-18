# Phone Audit Response

## Addressed

- Remote settings now exposes the security-critical fields that were hidden: `requirePin` and `localNetworkOnly`.
- Remote settings now exposes editable `port` and `autoStopMinutes` fields with ViewModel validation ranges.
- Phone ViewModel can update `requirePin`, `localNetworkOnly`, `port`, and `autoStopMinutes` through the existing DataStore-backed settings use case.
- Sync-to-watch now sends user-editable settings instead of mostly read-only defaults.
- Remote URL input now validates `http://` / `https://` before trying to open a browser.
- The remote manager button is labeled honestly as `Open in browser` until a real in-app remote manager exists.
- `Read latest status` was renamed to `Refresh watch status` and now sends a watch status command instead of only reading an in-memory snapshot.
- Phone transfer status card is now always visible and shows `No transfer yet` instead of disappearing.
- Clipboard messaging now tells users to open the target folder and then tap `Paste here`.
- Phone file list now has an empty-folder state.
- Phone home/file rows no longer depend on emoji glyphs.

## Still pending from phone audit

- Real in-app Remote File Manager with PIN, list/download/upload/delete flows.
- Editable transfer target path and real channel progress during phone-to-watch send.
- Share and Favorite actions in the phone file action sheet.
- Media bucket filters and richer media preview.
- Theme selector wired to `AppSettingsUseCase.setTheme`.
- Shared phone settings row component in `core-ui`.
- SAF/local operation progress unification.
- Remove dead phone state/classes after verifying no references.
