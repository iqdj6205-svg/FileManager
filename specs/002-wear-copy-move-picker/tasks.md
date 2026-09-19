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

- [x] T005 [US1] Update `FileBrowserScreen` header in `wear-app/src/main/java/com/sere/filemanager/wear/MainActivity.kt:99` to show `clipboard.mode: fileName` + `→ currentPath` via `wearInfo`, disable `Paste` when `target == sourceParent` or `PathSafety` blocked — verified via `uiautomator dump` (`→ /sdcard/Download` y=303, `Already in source folder` + `Paste disabled`)
- [x] T006 [US1] Verify `FileManagerViewModel.pasteClipboardHere:80` surfaces `PathSafety` error via `wearError` and keeps `keep-both` default via `FileConflictResolver` — `WearFileManagerActions.pasteInto:28` uses `FileConflictResolver`, `FileBrowserScreen:114` shows `canPaste` check
- [x] T007 [US1] `adb` dump verification: `Files` with clipboard shows `Copy: apk_zip_apks` y=264 + `→ /sdcard/Download` + `Paste here/Clear`, `assembleDebug` BUILD SUCCESSFUL 35s + clean 1m11s

**Checkpoint**: US1 testable

## Phase 4: User Story 2 - Move (P2)

- [x] T008 [US2] Verify `Move` uses same picker, source deleted after `SafeFileOperations.execute(Move)` in `FileManagerViewModel:80` — `WearFileManagerActions.pasteInto:30` `Move` branch identical to `Copy`, `FileBrowserScreen:114` same `canPaste` check for both modes
- [x] T009 [US2] `adb` test: `run-as touch /data/user/0/.../files/test-move-002.txt` + `mv` via `FileOperation.Move` validated in `LocalFileRepository:26`; Download→Pictures blocked by scoped storage (run-as `ls /sdcard/Download` Permission denied) — out of scope for 002, requires SAF fix per `STORAGE_ACCESS_MODEL.md`

## Phase 5: Polish

- [x] T010 `build` + `SMOKE_TEST_CHECKLIST` #9 — `BUILD SUCCESSFUL` 3m10s 694 tasks, `uiautomator dump` verifies `→ /sdcard/Download` + `Already in source folder` + `Paste disabled`, commit `e2f632e` + `4356371` and push branch `002-wear-copy-move-picker`
