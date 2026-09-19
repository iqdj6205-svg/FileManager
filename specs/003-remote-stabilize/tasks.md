---
description: "Tasks for Stabilize Remote Status List Download"
---

# Tasks: Stabilize Remote Status List Download

**Input**: spec.md, plan.md in `/specs/003-remote-stabilize/`

## Phase 1: Setup

- [x] T001 Branch `003-remote-stabilize` from `main`
- [x] T002 Build baseline `BUILD SUCCESSFUL`

## Phase 2: Foundational

- [x] T003 Confirm `RemoteAddressResolver`, `NetworkStatus`, `RemoteServerService`, `SimpleHttpEngine`, `PathSafety` exist
- [x] T004 Confirm `WearRemoteDashboard` uses `NetworkStatus.connectionLabel()` + `BatteryMonitor`

**Checkpoint**: Foundation ready

## Phase 3: User Story 1 - Start/stop + URL/PIN (P1) 🎯 MVP

- [x] T005 [US1] Fix `RemoteServerService.startServer` to use `RemoteAddressResolver.bestHttpUrl()` (local IP, not 127.0.0.1) + `NetworkStatus` local-only check, update `RemoteServerStatusStore` and `WearStatusPublisher`, handle `ForegroundServiceStartNotAllowedException` via `RemoteServiceController.start()` boolean — fixed `RemoteLifecyclePolicy.shouldAllowStart` + `ServiceBackedRemoteController` polling 2s, notification `http://192.168.0.23:8080`
- [x] T006 [US1] Verify `WearRemoteDashboard` shows `http://192.168` + PIN + `networkLabel` via `uiautomator dump` on SM_L705F, `isActive` check — dump shows `Server running` `State: Running` `URL: http://192.168.0.23:8080` `PIN: 716727` `Network: Wi‑Fi`
- [x] T007 [US1] `adb` curl: `pm grant` media, Start → `dumpsys activity services` `foregroundServiceType=dataSync`, Stop → `Stopped`, `BUILD SUCCESSFUL` — `isForeground=true foregroundId=42 types=0x00000001` + `Stopped`, `BUILD SUCCESSFUL 38s/694 tasks`

## Phase 4: User Story 2 - List (P2)

- [x] T008 [US2] Harden `SimpleHttpEngine.route` + `RemoteRouteExecutor.list` + `RemoteRequestValidator.validateRead` to require exact PIN (`RemoteAuth.isPinValid(stored, param)` not `length>=4`), `PathSafety` traversal guard — `PathSafety` now blocks `..`/`~`/empty + `/system`, `RemoteRequestValidator` + `SimpleHttpEngine.validateRoutePolicy` via `RemoteAuth.isPinValid`, `HttpResponseWriter` 32KB buffer
- [x] T009 [US2] `adb` curl: `GET /api/list?path=/sdcard/Download&pin=correct` → 200 JSON with `apk_zip_apks`, wrong PIN → 401, traversal `..` → 403 — `curl http://192.168.0.23:8080/api/list?path=/sdcard/Download&pin=716727` → `apk_zip_apks`, `401` wrong PIN, `403 Invalid or unsafe path.` for `..`

## Phase 5: User Story 3 - Download (P3)

- [x] T010 [US3] Ensure `SimpleHttpEngine.downloadResponse` streams via `DownloadPlanner` + `HttpResponse.FileStream` (32KB buffer, no RAM load), audit `DownloadRequested` — `HttpResponseWriter` 32KB `copyTo`, `SimpleHttpEngine.downloadResponse` via `DownloadPlanner` + `HttpResponse.FileStream` + `auditSink Download`/`auditLog DownloadRequested`
- [x] T011 [US3] `adb` curl: `GET /api/download?path=/sdcard/Download/WFM.png&pin=correct` → binary `200`, size matches `ls -la` — `curl /api/download?path=/sdcard/Download/WFM.png&pin=716727` → `200 image/png 1303664 bytes` matches `ls -la 1303664`

## Phase 6: Polish

- [x] T012 `WebManagerPage.render` shows active `RemoteConfig` (port, `requirePin`, `localNetworkOnly`), `build` + `SMOKE_TEST_CHECKLIST` remote #1-5, commit `feat(remote): stabilize status/list/download [003]` and push branch — `WebManagerPage` renders `local Wi‑Fi only` + port/pin/upload/delete, `BUILD SUCCESSFUL 1m44s 694 tasks`, Stop → `Stopped`
