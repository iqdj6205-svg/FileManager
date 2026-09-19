# Feature Specification: Storage Analyzer and File Search Recursive

**Feature Branch**: `005-storage-analyzer`
**Created**: 2026-09-19
**Status**: Draft
**Input**: User description: "Use recursive scanner in StorageAnalyzer and FileSearch"

## User Scenarios & Testing

### User Story 1 - Storage Analyzer recursive (Priority: P1)

User opens Phone Files → Analyze → expects analysis of all nested files under `/sdcard/Download`, not just one level, using `RecursiveScanner` with limit 1000 and cancellation support.

**Why this priority**: Alpha #4 analyzer currently shows only top level, misleading for nested folders.

**Independent Test**: On phone `SM_S928B`, `Analyze` on `/sdcard/Download` returns `totalFiles` includes nested files in `apk_zip_apks` subfolder.

**Acceptance Scenarios**:
1. **Given** `/sdcard/Download` with `Dir/sub/file.txt`, **When** `StorageAnalyzer.analyze("/sdcard/Download")`, **Then** `totalFiles` includes `file.txt` via `RecursiveScanner`.
2. **Given** large tree >1000 files, **When** scan, **Then** respects `maxFiles=1000` and `shouldContinue`.

### User Story 2 - FileSearch recursive (Priority: P2)

User searches `WFM` in `/sdcard/Download` and expects results from subfolders via `RecursiveScanner`.

**Why this priority**: Spec requires recursive search, current is one-level filter.

**Independent Test**: `FileSearch.search("/sdcard/Download","WFM")` finds `WFM.png` even if inside subfolder.

**Acceptance Scenarios**:
1. **Given** nested `sub/WFM.png`, **When** search `WFM`, **Then** result includes `sub/WFM.png`.
2. **Given** blank query, **When** search, **Then** empty list.

### Edge Cases

- What happens when path is protected `/system`? → `PathSafety` guard prevents scan, returns empty/error.
- How does system handle `shouldContinue` false during mid-scan? → stops early, returns partial list.

## Requirements

### Functional Requirements

- **FR-001**: System MUST `StorageAnalyzer.analyze` use `RecursiveScanner` (not `repository.list` one-level) with `maxFiles=1000`.
- **FR-002**: System MUST `FileSearch.search` use `RecursiveScanner` with case-insensitive `contains` on nested results.
- **FR-003**: System MUST keep `PathSafety` check before scanning.

### Key Entities

- **StorageAnalysis**: `{ path, totalFiles, totalBytes, categories, largestFiles }`
- **FileItem**: `{ path, name, type, sizeBytes }`

## Success Criteria

- **SC-001**: `StorageAnalyzer.analyze("/sdcard")` totalFiles > one-level list count when nested exists.
- **SC-002**: `BUILD SUCCESSFUL` `--no-daemon build` 694 tasks.
- **SC-003**: `FileSearch.search` finds nested file via recursive scan.

## Assumptions

- Watch and phone on same codebase; `RecursiveScanner` uses `LocalFileRepository.childToItem` for mapping.
- Limit 1000 files sufficient for alpha; pagination deferred.
