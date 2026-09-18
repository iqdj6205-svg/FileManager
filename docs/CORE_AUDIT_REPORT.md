# Core Audit Report

Source: external/local model audit provided by the user on 2026-09-18.

Trust level: **needs verification**. The model reportedly had prior analysis context, but the findings must not be trusted blindly. Treat every item as a hypothesis until confirmed against the repository and build.

Scope claimed by source:

- 100+ files read under `core-*/src/main/java/**`.
- 127 Kotlin files in total.
- Compared against the product/spec documents in `docs/` including architecture, product spec, storage analyzer, SAF, media, and remote documents.
- UI is excluded; this report is core-only.

## Executive summary from audit

- Approximately 35 files/types may be dead or duplicated and candidates for removal after verification.
- Main P0/P1 areas:
  - remote PIN/security and route consistency;
  - Android MediaStore implementation;
  - remote multipart upload support;
  - recursive file search/analyzer;
  - real file details/permissions;
  - storage safety/path guards;
  - persistence for favorites/recent;
  - codec/serialization hardening for Wear bridge;
  - theme/app settings integration.

## Important correction against latest code

Some reported issues may already be partially addressed in later commits:

- Remote API exact PIN enforcement was addressed in `948de3f`.
- Remote config is now passed into the embedded HTTP server in `78c4ace`.
- Remote start/status improvements continued through `1ff1d86`, `b67ef45`, `2f3d5fb`, `0e77b2e`, and `2dad0a8`.

Still verify all remote/security items before closing them.

---

## 1. `core-model`

Claimed scope: 15 files, 8 dead / 4 duplicates.

| File / type | Audit finding | Type | Status |
| --- | --- | --- | --- |
| `TransferModels.kt`: `FileTransfer`, `TransferDirection`, `TransferState` | Duplicates `core-wear-bridge/WearBridgeFileTransfer.kt` (`WearFileTransferRequest`, `WearFileTransferProgress`, `WearTransferDirection`, `WearTransferState`). Used only by `phone-app/TransferQueue.kt`, which is claimed dead. | duplicate / dead | needs verification |
| `CompanionModels.kt`: `CompanionState`; `CompanionProtocol.kt`: `CompanionCommand` | Duplicates `core-wear-bridge/WearBridgeCommand.kt`; `PhoneAppState.companion` reportedly never written/read. | duplicate / dead | needs verification |
| `BrowserModels.kt`: `FileSortMode`, `BrowserPreferences` | Duplicates `core-files/FileBrowserUseCase.kt` `SortMode`. `BrowserPreferences.favorites/recent` duplicates repositories. | duplicate | needs verification |
| `ProductMode.kt`: `ProductMode`, `ProductCapability` | Claimed zero references outside file. | dead | needs verification |
| `UserMessages.kt`: `UserMessage`, `Kind` | Claimed zero references. | dead | needs verification |
| `StorageModels.kt`: `OperationProgress`, `BatteryPolicy` | `OperationProgress` unused; `BatteryPolicy` duplicates `RemoteConfig.autoStopMinutes` / stop-below-percent behavior. | dead / duplicate | needs verification |
| `DiagnosticsModels.kt`: `DiagnosticsReport` | Used only by `core-remote/DiagnosticsExporter.kt`, which is claimed dead. | chain-dead | needs verification |
| `StorageAccessGuide.kt`: `DefaultStorageAccessGuides` | Claimed zero references; guides are manually assembled in Wear screens. | dead | needs verification |
| `FileManagerScreenModels.kt`: `DefaultHomeActions` | Live, used by Wear/Phone home, but emoji may not render on Wear. | live with UI defect | needs verification |
| `FileItem.kt`: `FileItemType` | Live, but type detection duplicated across `LocalFileRepository` and `SafFileOperations`. | duplicate logic | needs verification |
| `RemoteModels.kt`: `RemoteSession` | Live. | ok | verify |
| `RemoteServerSettings.kt` | Live; canonical for remote settings models/payload. | ok | verify |
| `PermissionModels.kt` | Live. | ok | verify |

Audit conclusion: 8 files may be removable, 4 duplicates should be unified.

---

## 2. `core-files`

Claimed scope: 30 files, 12 dead, 5 placeholders/stubs.

| File / area | Audit finding | Status |
| --- | --- | --- |
| `FileSearch.kt` | Searches only current directory with `repository.list(path).filter { name.contains(query) }`; spec requires recursive search. | needs verification |
| `StorageAnalyzer.kt` | Analyzes one level only; `RecursiveScanner.kt` exists but is unused. | needs verification |
| `RecursiveScanner.kt` | Real recursive walker with `maxFiles=1000` and `shouldContinue`, but zero references. | needs verification |
| `FileDetails.kt` | `canRead=true`, `canWrite=true` hardcoded; should use real file capabilities. | needs verification |
| `DuplicateFinder.kt` | Naive duplicate detection by lowercased name + size; reportedly unused. | needs verification |
| `BrowserHistory.kt` | Back/forward stack exists, but ViewModels reportedly store only `currentPath`. | needs verification |
| `RecentFilesRepository.kt` | In-memory only, no persistence; spec requires DataStore/Room. | needs verification |
| `FavoritesRepository.kt` | In-memory only; UI only reports favorite updated. | needs verification |
| `FileRepositoryExtensions.kt` | `FileRepositoryDefaults.likelyWearRoots()` unused; roots hardcoded elsewhere. | needs verification |
| `FileOpenPolicy.kt` | Policy exists but UI reportedly does not call `decide()`. | needs verification |
| `OperationNamePolicy.kt` | `duplicateName()` duplicates Wear ViewModel copy naming with inconsistent naming (`copy` vs `copy-`). | needs verification |
| `SafTreeRepository.kt` / `SafTreeAccess.kt` | Claimed unused; SAF real path goes through `PhoneSafController` → `SafFileOperations`. | needs verification |
| `StorageInsights.kt` | Live but minimal: only no files, large file >100MB, archives. | needs expansion |
| `PathSafety.kt` | Guard too narrow; does not catch `..` traversal and `/data/data`. | needs verification / likely P0 |
| `FileBrowserUseCase.kt` | Live but duplicates hidden-file filtering logic with `FileSearch`. | needs verification |
| `SafeFileOperations.kt`, `SafFileOperations.kt`, `LocalFileRepository.kt` | Live. Progress sinks may be isolated; local operation progress may not reach Phone UI. | needs verification |
| `StorageAccessManager.kt` | Live. | verify |
| `AppSettingsUseCase.kt` | Live. | verify |

---

## 3. `core-media`

Claimed scope: 14 files, 6 dead, 2 placeholders.

| File / area | Audit finding | Status |
| --- | --- | --- |
| `InMemoryMediaRepository.kt` | Returns empty list; unused because production uses `AndroidMediaStoreRepository`. | needs verification |
| `InMemoryMediaPlaybackController.kt` | StateFlow only, no ExoPlayer; production uses Android Media3 controller. | needs verification |
| `MediaClassifier.kt` | Trivial wrapper around `FileItemType`; zero references. | needs verification |
| `MediaSearch.kt` | Search/filter logic needs verification for completeness and usage. | needs verification |
| `ThumbnailPolicy.kt` / `ThumbnailPolicyChecker.canGenerate()` | Zero references; battery policy not applied before Coil thumbnails. | needs verification |
| `PlaybackLifecycle.kt` | Zero references; ViewModel calls controller directly. | needs verification |
| `ImagePreviewModels.kt` | `Share` / `Details` actions are placeholders (`queued`), no Android intent integration. | needs verification |
| `AndroidMediaStoreRepository.kt` | Uses deprecated/problematic `MediaColumns.DATA`; should use `RELATIVE_PATH` and `ContentUris`; hardcoded limit/pagination issues. | P0/P1 verify |
| `MediaLibraryUseCase.kt` | Live, but query limit reportedly applied after loading multiple categories, not at query level. | needs optimization |
| `ImagePreviewModels.kt`, `MediaSessionModels.kt`, `MediaPlaybackModels.kt` | Live. | verify |

---

## 4. `core-remote`

Claimed scope: 30+ files, 11 dead, 3 critical logic issues.

| File / area | Audit finding | Status |
| --- | --- | --- |
| `HttpFileServer.kt` | Facade/stub with fake loopback URL, reportedly unused; real server is `EmbeddedHttpFileServer` + `SimpleHttpEngine`. | needs verification |
| `RemoteWriteController.kt` + `RemoteWriteRequests.kt` | Duplicates `RemoteRouteExecutor` rename/delete/mkdir; reportedly dead. | needs verification |
| `RemoteUploadRequestParser.kt` / `HttpRequest.kt` | Old text parser; real parser is `HttpRawRequestParser`. | needs verification |
| `UploadModels.kt` | Upload planner/plan/limits reportedly unused; validation uses `RemoteUploadPolicy`. | needs verification |
| `RemoteServerHealth.kt`, `RemoteFeatureFlags.kt`, `HttpResponses.kt` | Claimed dead; feature flags may be logically wrong if rename is tied to delete flag. | needs verification |
| `DiagnosticsExporter.kt` | Dead with `DiagnosticsReport`. | needs verification |
| `RemoteStatus.kt` | Dead; `RemoteStatusJson.render` uses `RemoteSession` directly. | needs verification |
| `InMemoryRemoteServerController.kt` | Used only as factory fallback; production always uses embedded server. | needs verification |
| `RemoteRequestValidator.kt` PIN logic | Audit reported P0: previously allowed null/any 4+ char PIN. **Partially addressed in `948de3f`; verify current behavior.** | verify fixed |
| `RemoteUploadWriter.kt` | Raw body only, no multipart/form-data parser; spec requires multipart. | P1 |
| `SimpleHttpEngine.kt` | No `SO_REUSEADDR`; local-only policy is enforced at service start, but socket binding still listens broadly. Audit says client IP only partly represented in audit. | needs verification |
| `RemoteRouteExecutor.status()` | Writes audit entry for every status read, creating noise. | needs verification |
| Live remote stack | `SimpleHttpEngine`, `EmbeddedHttpFileServer`, `RemoteRouteExecutor`, `RemoteRequestValidator`, `RemoteAuth`, `RemoteSessionManager`, `RemoteAudit*`, `RemoteDirectorySerializer`, `RemoteUploadPolicy`, `DownloadPlanner`, `WebManagerPage`. | verify |

---

## 5. `core-ui` + `core-wear-bridge`

Claimed scope: 13 files.

| File / area | Audit finding | Status |
| --- | --- | --- |
| `core-ui/Formatters.kt`: `UiFormatters.compactBytes` | Duplicates `core-files/StorageFormatter.bytes()`. | needs verification |
| `core-ui/AppTheme.kt`: `FileManagerTheme` | Always dark; ignores `AppSettings.theme` despite System/Dark/Light requirement. | needs verification |
| `core-ui/DesignTokens.kt` | Audit claimed zero references. Note: latest UI migration likely added references; verify against latest code. | needs verification / may be outdated |
| `core-wear-bridge/WearBridgeCodec.kt` | Uses separator-based string codec with `\u001f`; fragile and lossy if payload contains separator. | P1 |
| `WearBridgeFileTransfer.kt`, `WearBridgeCommand.kt`, `WearBridgePaths.kt` | Live. `WearFileTransferRequest.targetPath` may be built unsafely from target directory + display name without sanitizing `..`. | needs verification / likely P1 |
| `WearBridgeStatusCodec.kt`, `WearTransferProgressCodec.kt`, `WearBridgeSettingsStringCodec.kt` | Live; nullable `DataMap.getString()` issue reportedly already fixed. | verify |

---

## Audit-prioritized backlog

### P0 — verify/fix before beta

- [ ] Verify current `RemoteRequestValidator` + `SimpleHttpEngine` exact PIN behavior after `948de3f`.
- [ ] Replace `AndroidMediaStoreRepository` reliance on `MediaColumns.DATA` with URI-first/RELATIVE_PATH-safe handling.
- [ ] Harden path safety against `..`, `/data/data`, and unsafe transfer targets.

### P1 — finish started core behavior

- [ ] Implement multipart upload support or explicitly constrain web manager upload to raw-body behavior.
- [ ] Use recursive scanner in `FileSearch` and `StorageAnalyzer` instead of one-level scans.
- [ ] Replace hardcoded file detail permissions with real readability/writability where possible.
- [ ] Unify duplicate file type detection logic.
- [ ] Unify duplicate byte formatters.
- [ ] Make favorites/recent persistent or mark UI as temporary everywhere.
- [ ] Make local file operation progress visible consistently in Phone UI.
- [ ] Replace separator-based Wear bridge codecs with safer structured encoding.
- [ ] Sanitize Wear bridge transfer target paths and filenames.
- [ ] Make `FileManagerTheme` respect System/Dark/Light settings.

### P2 — cleanup after build is stable

- [ ] Delete or merge verified-dead model files.
- [ ] Delete or merge verified-dead remote facade/parser/write classes.
- [ ] Delete or merge unused media stubs.
- [ ] Remove duplicate browser/history/preference abstractions or wire them properly.
- [ ] Expand storage insights beyond the current minimal rules.

## Handling rules

- Do not delete the claimed-dead files blindly.
- First verify references with repository search/build.
- Prefer consolidating one vertical area at a time.
- If a file is only unused because UI is not wired yet but represents a planned feature, either wire it or document it as planned.
- Keep commits small enough to bisect, but group related core cleanup logically.
