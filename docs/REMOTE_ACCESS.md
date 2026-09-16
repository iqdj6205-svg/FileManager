# Remote Access Design

## First protocol: HTTP

HTTP is the first target because it works from any phone, tablet, desktop, or browser without extra software.

## Server behavior

- Off by default
- User starts it explicitly from the watch
- PIN/session authentication
- Local-network-only default
- Upload/delete disabled unless explicitly enabled
- Auto-stop after timeout
- Stop on low battery

## Planned API

- `GET /` — web manager
- `GET /api/status` — server/session status
- `GET /api/list?path=/path` — list directory
- `GET /api/download?path=/path/file` — download file
- `POST /api/upload` — upload file, if enabled
- `POST /api/rename` — rename, if enabled
- `POST /api/delete` — delete, if enabled
- `POST /api/mkdir` — create folder

## Why not FTP first?

FTP is familiar but has security and compatibility problems. WebDAV may be a better future protocol for desktop file manager integration. HTTP web management is the safest first milestone.
