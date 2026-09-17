# SAF Implementation

## Added

- `SafTreeAccess` for persisted tree permissions and display names.
- `SafFileOperations` for listing, creating folders, renaming and deleting via Android `DocumentFile`.
- `StorageRootResolver` to distinguish path roots and SAF roots.
- `PhoneSafController` for app-level SAF access.
- Phone ViewModel factory injects the SAF controller.

## Remaining

- Use SAF controller for selected SAF roots in the phone browser.
- Add create-folder input UI instead of fixed quick names.
- Add proper copy/move streams between path roots and SAF roots.
- Persist custom SAF roots across restarts.
