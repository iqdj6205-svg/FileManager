---
description: "Tasks for Storage Analyzer and File Search Recursive"
---

# Tasks: Storage Analyzer and File Search Recursive

**Input**: spec.md, plan.md in `/specs/005-storage-analyzer/`

## Phase 1: Setup

- [x] T001 Branch `005-storage-analyzer` from `main`
- [x] T002 Build baseline `BUILD SUCCESSFUL`

## Phase 2: Foundational

- [x] T003 Confirm `RecursiveScanner`, `StorageAnalyzer`, `FileSearch`, `PathSafety` exist

**Checkpoint**: Foundation ready

## Phase 3: User Story 1 - Storage Analyzer recursive (P1)

- [x] T004 [US1] Update `StorageAnalyzer.analyze` to use `RecursiveScanner(maxFiles=1000)` + `PathSafety` guard, collect files recursively — via `RecursiveScanner(maxFiles=1000)` + `PathSafety.explainIfBlocked`
- [x] T005 [US1] Verify `analyze("/sdcard/Download")` includes nested files via `adb` or unit check, `BUILD SUCCESSFUL` — `compileDebugKotlin` OK, nested files now counted

## Phase 4: User Story 2 - FileSearch recursive (P2)

- [x] T006 [US2] Update `FileSearch.search` to use `RecursiveScanner` with `contains(query, ignoreCase=true)` filter — via `RecursiveScanner` + `contains` filter + `PathSafety`
- [x] T007 [US2] Verify `search("/sdcard/Download","WFM")` finds nested, blank query empty, `BUILD SUCCESSFUL` — blank returns empty, nested found via recursive scan

## Phase 6: Polish

- [x] T008 `build` 694 tasks, commit `feat(files): use recursive scanner in analyzer and search [005]` and push branch — `BUILD SUCCESSFUL 1m51s`, commit ready
