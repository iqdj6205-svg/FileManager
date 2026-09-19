# Implementation Plan: Stabilize Remote Status List Download

**Branch**: `003-remote-stabilize` | **Date**: 2026-09-19 | **Spec**: `specs/003-remote-stabilize/spec.md`
**Input**: Spec from `/specs/003-remote-stabilize/spec.md` (ALPHA #10, #4-5)

## Summary

Stabilize Wear remote minimum: `RemoteServerService` shows local IP (not 127.0.0.1) via `RemoteAddressResolver`, `FGS dataSync` + PIN exact check, `SimpleHttpEngine` exposes `GET /api/status`, `GET /api/list`, `GET /api/download` streaming, with `PathSafety` traversal guard and `localNetworkOnly` binding.

## Technical Context

**Language/Version**: Kotlin 2.0.20, Java 17, AGP 8.6.1
**Primary Dependencies**: `core-remote` (`SimpleHttpEngine`, `EmbeddedHttpFileServer`, `RemoteRouteExecutor`, `RemoteRequestValidator`, `RemoteAuth`, `DownloadPlanner`, `RemoteAddressResolver`), `wear-app` (`RemoteServerService`, `WearRemoteDashboard`, `NetworkStatus`), `core-files` (`PathSafety`)
**Storage**: Local `File` via `LocalFileRepository` + `RemoteDirectorySerializer`
**Testing**: `adb` curl on SM_L705F (`192.168.x.x:8080`), `uiautomator dump`, `BUILD SUCCESSFUL`
**Target Platform**: Wear OS 4+ (minSdk 30)
**Project Type**: Mobile (Wear) + `core-remote`
**Performance Goals**: `list` <500ms, `download` streaming 32KB buffer, no RAM load
**Constraints**: `allowUploads/Delete false` default, `requirePin true`, `ForegroundServiceStartNotAllowedException` catch
**Scale/Scope**: 3 endpoints, ~80 LOC, `WebManagerPage` shows active config

## Constitution Check

- I Pre-alpha: honest scope (3 endpoints only) — PASS
- II Build Green: `--no-daemon build` — PASS
- III Wear Standalone: server on watch — PASS
- IV Unified Design: `WearRemoteDashboard` compact — PASS
- V Check-Fix-Commit-Test: `adb` curl + dump — PASS

No violations.

## Project Structure

```text
specs/003-remote-stabilize/
├── spec.md
├── plan.md          # This file
└── tasks.md         # next

wear-app/src/main/java/com/sere/filemanager/wear/
├── RemoteServerService.kt       # startServer with RemoteAddressResolver + localNetworkOnly check
├── WearRemoteDashboard.kt       # shows http://192.168 + PIN + network
├── NetworkStatus.kt / RemoteAddressResolver.kt
└── ui/WearUi.kt

core-remote/src/main/java/com/sere/filemanager/core/remote/
├── SimpleHttpEngine.kt          # route, validateRoutePolicy, downloadResponse streaming
├── RemoteRouteExecutor.kt       # list, status
├── RemoteRequestValidator.kt    # validateRead exact PIN
├── RemoteAuth.kt                # isPinValid
└── WebManagerPage.kt            # render with config

core-files/src/main/java/com/sere/filemanager/core/files/
└── PathSafety.kt
```

**Structure Decision**: Feature touches `wear-app` FGS + `core-remote` HTTP, no `phone-app`.

## Complexity Tracking

No violations.
