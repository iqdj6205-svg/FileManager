# Feature Specification: Real Wear Rename Input

**Feature Branch**: `001-wear-rename-input`
**Created**: 2026-09-19
**Status**: Draft
**Input**: User description: "Real Wear rename input with validation"

## User Scenarios & Testing

### User Story 1 - Rename file with real input (Priority: P1)

User on Wear long-presses a file, chooses Rename, edits name via system input (keyboard/voice), taps Save, file is renamed, browser shows updated name and message. Currently `RenameInputScreen` uses presets `copyName`/`lowercase` only — no real text input.

**Why this priority**: Alpha target #8 in `ALPHA_STABILIZATION_PLAN.md` — "Rename uses real user input, not generated placeholder". Without it, file operations are unusable.

**Independent Test**: On emulator/watch, open `/sdcard/Download`, long-press file → FileActions → Rename → system input appears, edit `photo.jpg` to `vacation.jpg` → Save → list shows `vacation.jpg`, operation message "Renamed", no `BUILD` error.

**Acceptance Scenarios**:
1. **Given** file `/sdcard/DCIM/test.txt` exists and permissions granted, **When** user renames to `notes.txt`, **Then** `FileRepository.rename` succeeds, `BrowserState.items` refreshes, `OperationUiState.message = "Renamed"`.
2. **Given** rename screen opened, **When** user leaves name blank and taps Save, **Then** Save is disabled / shows "Name cannot be empty", no repository call.
3. **Given** target name already exists in same dir, **When** user saves, **Then** `FileConflictResolver` applies `keep-both` or shows `400` error from `SafeFileOperations`, message displayed via `OperationMessageScreen`.

---

### User Story 2 - Validation and conflict handling (Priority: P2)

Input validates before call.

**Why this priority**: Prevents `FileOperationValidator` / `OperationNamePolicy.isValidFileName` bypass and confusing 400 errors.

**Independent Test**: Try names `""`, `"."`, `".."`, `"a/b"`, 200-char string → UI shows inline error, Save disabled.

**Acceptance Scenarios**:
1. **Given** input `""` or `"."`, **When** editing, **Then** helper text "Name cannot be empty" / "Invalid name".
2. **Given** name contains `/` or `\`, **When** typed, **Then** sanitized via `OperationNamePolicy.sanitizeInputName` or blocked with message.

### Edge Cases

- What happens when file was deleted externally between `selectedItem` and Save? → `rename` returns `Result.failure("Cannot rename file")`, `operation.message` shows error, browser stays on current path.
- How does system handle `..` traversal attempt? → `PathSafety.explainIfBlocked` + `isValidFileName` rejects.
- Name >160 chars? → truncated / rejected per `OperationNamePolicy`.
- Voice input returns empty? → same as blank validation.

## Requirements

### Functional Requirements

- **FR-001**: System MUST show `RenameInputScreen` with `BasicTextField` prefilled with `fileItem.name` (not `copyName`) and system keyboard/voice via `RemoteInput` / `BasicTextField` focus.
- **FR-002**: System MUST validate via `OperationNamePolicy.isValidFileName` and `sanitizeInputName`; Save enabled only when `value != original` and valid.
- **FR-003**: System MUST call `viewModel.renameSelected(newName)` → `FileRepository.rename` → `SafeFileOperations.execute(FileOperation.Rename)` and refresh `openPath(current)`.
- **FR-004**: System MUST display result via `OperationUiState.message` (`ActionMessages.from`) and `OperationMessageScreen` with `wearTitle`/`wearInfo` (unified design).
- **FR-005**: System MUST keep navigation: `Files → FileActions → Rename → (Save/Cancel) → Files` via `WearNavigator` (`WearScreen.Rename`).

*No clarification needed — uses existing `FileRepository`, `PathSafety`, `DesignTokens`.*

### Key Entities

- **FileItem**: `{ name, path, type }` — source of rename.
- **RenameInputState**: `{ originalName, currentValue, isValid, errorMessage }` — derived in composable, not new model.

## Success Criteria

- **SC-001**: Rename completes on watch emulator in <5s end-to-end (open → edit → save → list refreshed).
- **SC-002**: 100% of invalid inputs (`""`, `"."`, `".."`, `"/"`, `>160`) blocked client-side before repository call.
- **SC-003**: `BUILD SUCCESSFUL` with `WearUi` (`WearRotaryList`/`wearPrimaryAction`) — no `ScalingLazyColumn` with hardcoded `18.dp`.

## Assumptions

- Wear `minSdk 30` has `BasicTextField` + system keyboard available (fallback voice).
- `LocalFileRepository.rename` and `SafFileOperations` not needed for Wear local rename scope.
- Single file rename only; batch rename out of scope.
