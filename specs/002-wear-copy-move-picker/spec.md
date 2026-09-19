# Feature Specification: Wear Copy/Move Destination Picker

**Feature Branch**: `002-wear-copy-move-picker`
**Created**: 2026-09-19
**Status**: Draft
**Input**: User description: "Wear copy move destination picker with safe conflict handling"

## User Scenarios & Testing

### User Story 1 - Copy with destination picker (Priority: P1)

User selects file → Copy → navigates to destination folder → Paste. Existing flow uses hidden clipboard (`WearClipboardState`) with "Paste here" chip only in `FileBrowserScreen` header, no explicit destination confirmation, conflict silently handled by `FileConflictResolver`. Need explicit picker with target path display and safe overwrite prompt.

**Why this priority**: ALPHA #9 — copy/move must have clear destination picker and safe conflict behavior. Without it, user loses data on overwrite.

**Independent Test**: Create `test.txt` in `/Download`, Copy, navigate to `/Documents`, Paste → file appears in `/Documents/test.txt`, original remains, `OperationMessageScreen` "Copied".

**Acceptance Scenarios**:
1. **Given** clipboard has `Copy(test.txt)`, **When** user opens `/Documents` and taps Paste, **Then** `SafeFileOperations.execute(Copy)` succeeds, `openPath(/Documents)` refreshes, message "Copied".
2. **Given** destination already has `test.txt`, **When** Paste, **Then** `FileConflictResolver` prompts "Keep both / Replace / Cancel" (default keep-both → `test copy.txt`), no silent overwrite.

---

### User Story 2 - Move with same picker (Priority: P2)

Same picker but `Move` deletes source after copy.

**Why this priority**: Same UI, different `FileOperation.Move` semantics.

**Independent Test**: Move `test.txt` from `/Download` to `/Pictures` → source deleted, destination has file.

**Acceptance Scenarios**:
1. **Given** clipboard `Move(test.txt)`, **When** Paste in `/Pictures`, **Then** source deleted, `openPath(/Pictures)` shows file, `openPath(/Download)` no longer shows it.

### Edge Cases

- What happens when destination is same folder as source? → `FileOperationValidator` returns "Source and target are same", Paste disabled.
- How does system handle `..` traversal in target path? → `PathSafety.explainIfBlocked` blocks, Paste disabled, `wearError` shown.
- Clipboard cleared on `clearClipboard` or after successful paste — `WearClipboardBanner` hides.
- Paste on protected path `/system` → `PathSafety` blocks, `wearError` "This path is protected."

## Requirements

### Functional Requirements

- **FR-001**: System MUST show `WearClipboardBanner` with `mode: fileName` and target path subtitle in `FileBrowserScreen` header when `hasEntry`, using `WearRotaryList` + `DesignTokens`.
- **FR-002**: System MUST keep `markSelectedForCopy/Move` → `WearClipboardState` + `OperationUiState.message = clipboard.message` as now, but `pasteClipboardHere` MUST show `wearError` on `PathSafety` block and `FileConflictResolver` prompt on conflict (keep-both default).
- **FR-003**: System MUST refresh `openPath(target)` after paste and clear clipboard on success (existing `pasteClipboardHere:80` does, keep).
- **FR-004**: System MUST disable `Paste here` chip when `target == source parent` or `PathSafety` blocked.
- **FR-005**: Destination path display MUST be `state.browser.currentPath` with `maxLines 1` `ellipsis` via `wearInfo`.

### Key Entities

- **WearClipboardState**: `{ mode: Copy|Move, fileName, hasEntry }` — existing, no new fields.
- **FileOperation**: `Copy(sourcePath, targetPath)`, `Move(...)` — existing via `SafeFileOperations`.

## Success Criteria

- **SC-001**: Copy and Move each complete in <5s on watch emulator, with visible destination path and conflict handling, no silent overwrite.
- **SC-002**: `BUILD SUCCESSFUL` with compact `WearUi` (no `18.dp` hardcodes).
- **SC-003**: `uiautomator dump` on `FileBrowserScreen` with clipboard shows `Paste here` chip and target path text.

## Assumptions

- `LocalFileRepository.copy/move` already handles `overwrite` via `FileConflictResolver`; UI just surfaces choice (keep-both default, no new dialog library).
- Single file copy/move only; recursive dir copy out of scope (covered by `RecursiveScanner` later).
- `SAF` for watch not required for alpha — local `File` paths only.
