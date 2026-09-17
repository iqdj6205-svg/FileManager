# Build Fixes

## Latest fixes

- Added missing `FileManagerViewModel.onPermissionsResult`.
- Added missing `FileManagerViewModel.toggleFavoriteSelected`.
- Kept file operation execution behind `SafeFileOperations`.
- Added `FileRepositoryDefaults` for future root discovery expansion.
- Migrated module Gradle scripts from deprecated `kotlinOptions` to Kotlin `compilerOptions`.
- Aligned Java compile target with Kotlin JVM target: Java 17 / Kotlin JVM 17 in every module.
- Fixed `core-media` unresolved `FileRepository` by adding dependency on `:core-files`.
- Fixed Wear lint error `WearStandaloneAppFlag` by adding `com.google.android.wearable.standalone=true` metadata to the Wear manifest.

## Current user build issues fixed

### JVM target mismatch

Gradle failed with inconsistent JVM target:

- Java compile task: 1.8
- Kotlin compile task: 17

Fix: added `compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }` to all Android modules.

### core-media unresolved core-files references

`MediaLibraryUseCase` imports `com.sere.filemanager.core.files.FileRepository`, but `core-media` did not depend on `:core-files`.

Fix: added `implementation(project(":core-files"))` to `core-media/build.gradle.kts`.

### Wear standalone lint error

Wear lint requires declaring whether the Wear app can work standalone.

Fix: added:

```xml
<meta-data
    android:name="com.google.android.wearable.standalone"
    android:value="true" />
```
