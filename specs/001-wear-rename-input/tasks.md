---
description: "Tasks for Real Wear Rename Input"
---

# Tasks: Real Wear Rename Input

**Input**: spec.md, plan.md in `/specs/001-wear-rename-input/`
**Prerequisites**: plan.md, spec.md

## Phase 1: Setup

- [x] T001 Verify branch `001-wear-rename-input` and constitution `1.0.0` filled
- [x] T002 Ensure `wear-app` builds `BUILD SUCCESSFUL` (`--no-daemon build` baseline `274f05a`)

## Phase 2: Foundational

- [x] T003 Confirm `OperationNamePolicy.isValidFileName / sanitizeInputName` and `PathSafety` exist in `core-files`
- [x] T004 Confirm `WearRotaryList` / `DesignTokens` available (`wear/ui/WearUi.kt`, `core/ui/DesignTokens.kt`)

**Checkpoint**: Foundation ready

## Phase 3: User Story 1 - Real rename input (P1) 🎯 MVP

**Goal**: Rename with system keyboard, validation, and refresh

**Independent Test**: On emulator, Files → long-press → FileActions → Rename → edit `test.txt` → Save → list shows new name, `BUILD SUCCESSFUL`

### Implementation for US1

- [ ] T005 [P] [US1] Update `RenameInputScreen` in `wear-app/src/main/java/com/sere/filemanager/wear/MainActivity.kt:101` to use `val uri` pattern, `OperationNamePolicy` validation, Save enabled only when valid & changed, error via `wearInfo`
- [ ] T006 [US1] Wire `FileManagerViewModel.renameSelected(newName)` call with `ActionMessages.from` result handling and `openPath(current)` refresh in `wear-app/src/main/java/com/sere/filemanager/wear/FileManagerViewModel.kt:88`
- [ ] T007 [US1] Update `WearNavigator` flow `Files → FileActions → Rename → Files` with `viewModel.selectedItem()` null guard in `MainActivity.kt:79`
- [ ] T008 [US1] Smoke test: manual rename on emulator + `.\gradlew.bat --no-daemon :wear-app:assembleDebug` must pass

**Checkpoint**: US1 independently testable

## Phase 4: User Story 2 - Validation (P2)

- [ ] T009 [P] [US2] Add inline validation for `""`, `"."`, `".."`, `"/"`, `>160` via `isValidFileName` in `MainActivity.kt:101` (`RenameInputScreen`)
- [ ] T010 [US2] Sanitize `/`, `\` via `sanitizeInputName` before repository call
- [ ] T011 [US2] Verify `FileOperationValidator.validate` and `PathSafety.explainIfBlocked` path guard

## Phase 5: Polish

- [ ] T012 Run `.\gradlew.bat --no-daemon build` and `docs/SMOKE_TEST_CHECKLIST.md` alpha #8, update `docs/AUDIT_TRACKER.md`
- [ ] T013 Commit `fix: real wear rename input` and push `001-wear-rename-input`

## Dependencies

- T005 → T006 → T007 → T008 → T009
- T009, T010 parallel after T008
