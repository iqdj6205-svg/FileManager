# Implementation Plan: Storage Analyzer and File Search Recursive

**Branch**: `005-storage-analyzer` | **Date**: 2026-09-19 | **Spec**: `specs/005-storage-analyzer/spec.md`

## Summary

Make `StorageAnalyzer` and `FileSearch` use `RecursiveScanner` (maxFiles 1000, shouldContinue) instead of one-level `repository.list`.

## Technical Context

**Language/Version**: Kotlin 2.0.20, Java 17, AGP 8.6.1
**Primary Dependencies**: `core-files` (`StorageAnalyzer`, `FileSearch`, `RecursiveScanner`, `PathSafety`, `LocalFileRepository`)
**Testing**: `BUILD SUCCESSFUL`, manual `adb` analyze on SM_S928B `/sdcard/Download`
**Target Platform**: Phone (also wear)
**Performance Goals**: Scan 1000 files <1s, cancel via shouldContinue
**Constraints**: Keep PathSafety guard

## Constitution Check

- Pre-alpha honesty: minimal scope (2 files)
- Build Green: `--no-daemon build`
- Wear standalone: scanner works on wear via LocalFileRepository

No violations.

## Project Structure

```text
core-files/src/main/java/com/sere/filemanager/core/files/
├── StorageAnalyzer.kt  # switch to RecursiveScanner
├── FileSearch.kt       # switch to RecursiveScanner
└── RecursiveScanner.kt # already exists with maxFiles
```

## Complexity Tracking

No violations.
