# Implementation Plan: Real Wear Rename Input

**Branch**: `001-wear-rename-input` | **Date**: 2026-09-19 | **Spec**: `specs/001-wear-rename-input/spec.md`
**Input**: Feature spec from `/specs/001-wear-rename-input/spec.md` (ALPHA_STABILIZATION_PLAN #8)

## Summary

Replace placeholder rename (`QuickTextInputPresets.copyName`) with real system text input on Wear: `RenameInputScreen` prefilled with `fileItem.name`, validated via `OperationNamePolicy`, calling `FileManagerViewModel.renameSelected(newName)` → `SafeFileOperations`. Unified design via `WearRotaryList`.

## Technical Context

**Language/Version**: Kotlin 2.0.20, Java 17, AGP 8.6.1
**Primary Dependencies**: Compose 1.7.4, Wear Compose 1.4.0, `androidx.compose.foundation.text.BasicTextField`, `core-files` (`FileRepository`, `OperationNamePolicy`, `PathSafety`), `core-ui/DesignTokens`
**Storage**: Local `File` via `LocalFileRepository` (no DB)
**Testing**: Manual smoke on emulator/watch + `BUILD SUCCESSFUL` (`--no-daemon build`), `docs/SMOKE_TEST_CHECKLIST.md`; no unit tests (NO-SOURCE)
**Target Platform**: Wear OS 4+ (`wear-app` minSdk 30, `targetSdk 35`)
**Project Type**: Mobile (Wear) + shared `core-*` libraries
**Performance Goals**: Rename <5s end-to-end, input focus <300ms
**Constraints**: Round screen, `BasicTextField` must request focus + rotary, no `Full Qualified` imports, `BUILD SUCCESSFUL` gate
**Scale/Scope**: 1 screen (`RenameInputScreen`), 1 ViewModel method, ~50 LOC

## Constitution Check

*GATE: Must pass before Phase 0 research.*

- I. Pre-alpha honesty: spec documents real gap (not percent) — PASS
- II. Build Green: `--no-daemon build` + imports check — PASS (verified `274f05a`)
- III. Wear Standalone First: feature touches only `wear-app` + `core-files` — PASS
- IV. Unified Design: uses `WearRotaryList`, `wearTitle`, `FileManagerText`, `DesignTokens` — PASS
- V. Check-Fix-Commit-Test: smoke checklist + small batch — PASS

No violations.

## Project Structure

### Documentation (this feature)

```text
specs/001-wear-rename-input/
├── spec.md
├── plan.md              # This file
├── research.md          # Phase 0 — not needed (existing BasicTextField, OperationNamePolicy)
├── data-model.md        # Phase 1 — N/A (no new entity, uses FileItem)
├── quickstart.md        # Phase 1 — smoke steps
└── tasks.md             # Phase 2 — next via /speckit.tasks
```

### Source Code (repository root)

```text
wear-app/src/main/java/com/sere/filemanager/wear/
├── MainActivity.kt              # WearNavigator, Rename route
├── FileManagerViewModel.kt      # renameSelected(newName)
└── ui/WearUi.kt                 # WearRotaryList

core-files/src/main/java/com/sere/filemanager/core/files/
├── OperationNamePolicy.kt       # isValidFileName / sanitize
├── FileRepository.kt / LocalFileRepository.kt
└── PathSafety.kt

core-ui/src/main/java/com/sere/filemanager/core/ui/
└── DesignTokens.kt
```

**Structure Decision**: Single-repo, multi-module — feature touches `wear-app` UI + `core-files` validation only.

## Complexity Tracking

No violations.
