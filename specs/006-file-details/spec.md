# Feature Specification: File Details Real Permissions

**Feature Branch**: `006-file-details`
**Created**: 2026-09-19
**Status**: Draft
**Input**: User description: "Fix FileDetailsReader to use real file permissions"

## User Scenarios & Testing

### User Story 1 - Real canRead/canWrite (Priority: P1)

File details screen shows actual readable/writable state via `File.canRead()`/`canWrite()` instead of hardcoded true, so protected/system paths show correctly not writable.

**Independent Test**: `FileDetailsReader.details(FileItem("/system/file",...)).canWrite == false` on device.

**Acceptance Scenarios**:
1. **Given** regular file `/sdcard/Download/WFM.png`, **When** details, **Then** `canRead true` `canWrite true` (if file exists).
2. **Given** non-existent or protected path, **When** details, **Then** `canRead/canWrite` reflect `File.exists` and permissions, not always true.

## Requirements

- **FR-001**: System MUST `FileDetailsReader.details` compute `canRead`/`canWrite` via `java.io.File(item.path).canRead()/canWrite()` with existence check.

## Success Criteria

- **SC-001**: `BUILD SUCCESSFUL`
- **SC-002**: Protected path shows `canWrite false`.
