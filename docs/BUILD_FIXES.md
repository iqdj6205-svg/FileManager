# Build Fixes

## Latest fixes

- Added missing `FileManagerViewModel.onPermissionsResult`.
- Added missing `FileManagerViewModel.toggleFavoriteSelected`.
- Kept file operation execution behind `SafeFileOperations`.
- Added `FileRepositoryDefaults` for future root discovery expansion.

## Next compile-risk targets

- Wear Compose `Switch` signatures.
- Manifest foreground service permission details on Android 14+.
- Kotlin `removeLastOrNull` availability depending on stdlib target.
- Official Gradle wrapper generation.
