# Build Fixes

## Latest fixes

- Added missing `FileManagerViewModel.onPermissionsResult`.
- Added missing `FileManagerViewModel.toggleFavoriteSelected`.
- Kept file operation execution behind `SafeFileOperations`.
- Added `FileRepositoryDefaults` for future root discovery expansion.
- Migrated module Gradle scripts from deprecated `kotlinOptions` to Kotlin `compilerOptions`.

## Current sync status from Serg

Android Studio Gradle sync completed successfully. Remaining output was deprecation warnings only, not build failures.

## Next compile-risk targets

- Wear Compose `Switch` signatures.
- Manifest foreground service permission details on Android 14+.
- Kotlin API availability depending on installed plugin/stdlib.
- Official Gradle wrapper generation.
