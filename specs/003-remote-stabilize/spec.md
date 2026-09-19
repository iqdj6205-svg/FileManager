# Feature Specification: Stabilize Remote Status List Download

**Feature Branch**: `003-remote-stabilize`
**Created**: 2026-09-19
**Status**: Draft
**Input**: User description: "Stabilize remote status list download"

## User Scenarios & Testing

### User Story 1 - Start/stop and show URL/PIN (Priority: P1)

Watch user opens Remote → Start → sees URL `http://192.168.x.x:8080` (not `127.0.0.1`), PIN `123456`, network `WiFi`, battery, via `RemoteServerService` foreground `dataSync` + `WearRemoteDashboard`. Stop cleans socket.

**Why this priority**: Alpha #10 — remote minimum must start/stop and show URL/PIN; without local IP browser cannot connect.

**Independent Test**: On SM_L705F, Remote → Start → dashboard shows `http://192.168...:8080 PIN ...`, `adb shell dumpsys activity services` shows `foregroundServiceType=dataSync`, Stop → `RemoteSession.Stopped`.

**Acceptance Scenarios**:
1. **Given** server stopped, **When** Start, **Then** `RemoteServerService.startServer` `RemoteAddressResolver.bestHttpUrl()` != `127.0.0.1`, `RemoteServerStatusStore` updated, `WearStatusPublisher` Data Layer, notification shows `URL PIN`.
2. **Given** server running, **When** Stop, **Then** `ServerSocket.close` no crash, `RemoteSession.Stopped`, `isActive false`.

### User Story 2 - List files via /api/list (Priority: P2)

Browser on same WiFi fetches `GET /api/list?path=/sdcard/Download&pin=...` → JSON directory listing via `RemoteRouteExecutor.list` + `RemoteDirectorySerializer`.

**Why this priority**: Alpha #4 — list is first remote API.

**Independent Test**: `curl "http://watch-ip:8080/api/list?path=/sdcard/Download&pin=123456"` returns `200` JSON with `name`/`type` for `apk_zip_apks`, not `403`.

**Acceptance Scenarios**:
1. **Given** server running with `requirePin=true`, **When** `GET /api/list` with correct PIN, **Then** `RemoteRequestValidator.validateRead` PASS, `FileRepository.list` JSON.
2. **Given** wrong PIN, **When** list, **Then** `401 invalidPin`.

### User Story 3 - Download file (Priority: P3)

`GET /api/download?path=/sdcard/Download/WFM.png&pin=...` streams file via `DownloadPlanner` + `HttpResponse.FileStream` without loading to RAM.

**Why this priority**: Alpha #5 — download proves streaming.

**Independent Test**: `curl -o WFM.png "http://.../api/download?path=/sdcard/Download/WFM.png&pin=..."` → file size matches `ls -la`, `200` binary.

**Acceptance Scenarios**:
1. **Given** file exists, **When** download with PIN, **Then** `200` `contentType` from `MimeTypes`, `downloadName`, streaming, audit `DownloadRequested`.

### Edge Cases

- What happens when `localNetworkOnly=true` and request from `127.0.0.1` vs WiFi IP? → `RemoteConfig` binding check, `NetworkStatus.isWifi` determines, `403` if remote not local.
- How does system handle `..` traversal `/api/list?path=/sdcard/../system`? → `PathSafety.explainIfBlocked` → `403 invalidPath`.
- What happens when port 8080 busy? → `ServerSocket` `BindException` → `RemoteSession.Error` with message, audit `Error`.

## Requirements

### Functional Requirements

- **FR-001**: System MUST start `RemoteServerService` via `RemoteServiceController.start()` safely (catch `ForegroundServiceStartNotAllowedException`) and show local IP via `RemoteAddressResolver` (not `127.0.0.1`) in `WearRemoteDashboard` + notification + `RemoteServerStatusStore`.
- **FR-002**: System MUST expose `GET /api/status` → `RemoteStatusJson.render(RemoteSession)` with `state, url, pin, battery, network`.
- **FR-003**: System MUST expose `GET /api/list?path&pin` via `SimpleHttpEngine.route` → `RemoteRouteExecutor.list` + `PathSafety` + PIN check (`RemoteAuth.isPinValid` exact match, not `length>=4`).
- **FR-004**: System MUST expose `GET /api/download?path&pin` via `DownloadPlanner.plan` + `HttpResponse.FileStream` streaming, `RemoteRequestValidator` + `RemoteAuth` exact PIN.
- **FR-005**: System MUST enforce `allowUploads=false` / `allowDelete=false` default (keep disabled, no multipart yet).

### Key Entities

- **RemoteSession**: `{ state, url, pin, startedAtMillis, errorMessage }`
- **RemoteConfig**: `{ port, requirePin, allowUploads, allowDelete, autoStopMinutes, localNetworkOnly }`

## Success Criteria

- **SC-001**: On watch, Start → `adb shell curl` list and download succeed with correct PIN, wrong PIN `401`, traversal `403`.
- **SC-002**: `BUILD SUCCESSFUL` (`--no-daemon build` 694 tasks).
- **SC-003**: `uiautomator dump` Remote shows `http://192.168` not `127.0.0.1`.

## Assumptions

- Watch and browser on same WiFi; `localNetworkOnly=true` default as per `RemoteServerSettings`.
- `EmbeddedHttpFileServer` prototype (`RemoteServerControllerFactory useEmbeddedPrototype=true`) is the server under test.
- Upload/delete/rename remain disabled/hidden behind experimental flag for alpha.
