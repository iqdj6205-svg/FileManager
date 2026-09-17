# Build Fixes

## Latest fixes

- Added missing `FileManagerViewModel.onPermissionsResult`.
- Added missing `FileManagerViewModel.toggleFavoriteSelected`.
- Kept file operation execution behind `SafeFileOperations`.
- Added `FileRepositoryDefaults` for future root discovery expansion.
- Migrated module Gradle scripts from deprecated `kotlinOptions` to Kotlin `compilerOptions`.
- Aligned Java compile target with Kotlin JVM target: Java 17 / Kotlin JVM 17 in every module.

## Current user build issue fixed

Gradle failed with inconsistent JVM target:

- Java compile task: 1.8
- Kotlin compile task: 17

Fix: added `compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }` to all Android modules.
