---
description: "Tasks for Wear Copy/Move Destination Picker"
---

# Tasks: Wear Copy/Move Destination Picker

**Input**: spec.md, plan.md in `/specs/002-wear-copy-move-picker/`

## Phase 1: Setup

- [x] T001 Branch `002-wear-copy-move-picker` from `main` (merged 001)
- [x] T002 Build baseline `BUILD SUCCESSFUL`

## Phase 2: Foundational

- [x] T003 Confirm `WearClipboardState`, `PathSafety`, `FileConflictResolver`, `SafeFileOperations` exist
- [x] T004 Confirm `WearRotaryList` compact layout (12dp vertical, 36dp chip)

**Checkpoint**: Foundation ready

## Phase 3: User Story 1 - Copy with picker (P1) 🎯 MVP

- [ ] T005 [US1] Update `FileBrowserScreen` header in `wear-app/src/main/java/com/sere/filemanager/wear/MainActivity.kt:99` to show `clipboard.mode: fileName` + `currentPath` via `wearInfo`, disable `Paste here` when `target == sourceParent` or `PathSafety` blocked
- [ ] T006 [US1] Verify `FileManagerViewModel.pasteClipboardHere:80` surfaces `PathSafety` error via `wearError` and keeps `keep-both` default via `FileConflictResolver`
- [ ] T007 [US1] `adb` dump verification: `Files` with clipboard shows `Paste here` + target path, copy `test.txt` Download→Documents, `assembleDebug` PASS

**Checkpoint**: US1 testable

## Phase 4: User Story 2 - Move (P2)

- [ ] T008 [US2] Verify `Move` uses same picker, source deleted after `SafeFileOperations.execute(Move)` in `FileManagerViewModel:80`
- [ ] T009 [US2] `adb` test: Move `test.txt` Download→Pictures, source gone, destination has file

## Phase 5: Polish

- [ ] T010 `build` + `SMOKE_TEST_CHECKLIST` #9, update `AUDIT_TRACKER.md`, commit `feat(wear): copy-move picker` and push branch
