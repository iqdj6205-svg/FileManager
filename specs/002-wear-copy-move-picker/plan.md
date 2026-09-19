# Implementation Plan: Wear Copy/Move Destination Picker

**Branch**: `002-wear-copy-move-picker` | **Date**: 2026-09-19 | **Spec**: `specs/002-wear-copy-move-picker/spec.md`
**Input**: Spec from `/specs/002-wear-copy-move-picker/spec.md` (ALPHA #9)

## Summary

Make Wear copy/move destination explicit: keep existing `WearClipboardState` + `pasteClipboardHere` flow, but surface target path, disable Paste on blocked/same-folder, and surface conflict via `FileConflictResolver` (keep-both default) with compact `WearUi`.

## Technical Context

**Language/Version**: Kotlin 2.0.20, Java 17, AGP 8.6.1
**Primary Dependencies**: `core-files` (`SafeFileOperations`, `FileOperation`, `PathSafety`, `FileConflictResolver`, `WearClipboardState`), `wear/ui/WearUi.kt`, `core/ui/DesignTokens`
**Storage**: Local `File` via `LocalFileRepository`
**Testing**: `adb` `uiautomator dump` on SM_L705F + `assembleDebug` BUILD SUCCESSFUL
**Target Platform**: Wear OS 4+ (minSdk 30)
**Project Type**: Mobile (Wear)
**Performance Goals**: Paste <5s, UI shows destination in <100ms
**Constraints**: Round screen, compact `WearRotaryList` 12dp vertical, no FQN imports
**Scale/Scope**: 1 screen (`FileBrowserScreen` header) +1 ViewModel method `pasteClipboardHere` already exists, ~30 LOC

## Constitution Check

- I Pre-alpha Honesty: spec documents gap — PASS
- II Build Green: `--no-daemon` + imports — PASS
- III Wear Standalone: touches `wear-app` + `core-files` only — PASS
- IV Unified Design: `WearRotaryList`, `wearInfo`, `DesignTokens` — PASS
- V Check-Fix-Commit-Test: `adb` dump verification — PASS

No violations.

## Project Structure

```text
specs/002-wear-copy-move-picker/
├── spec.md
├── plan.md          # This file
└── tasks.md         # next

wear-app/src/main/java/com/sere/filemanager/wear/
├── MainActivity.kt          # FileBrowserScreen header
├── FileManagerViewModel.kt  # markSelectedForCopy/Move, pasteClipboardHere, clearClipboard
└── ui/WearUi.kt

core-files/src/main/java/com/sere/filemanager/core/files/
├── WearClipboardState.kt (via WearOperationState)
├── FileOperation.kt
├── SafeFileOperations.kt
├── PathSafety.kt
└── FileConflictResolver.kt
```

**Structure Decision**: Single-repo, feature touches header UI + existing ViewModel logic only.

## Complexity Tracking

No violations.
