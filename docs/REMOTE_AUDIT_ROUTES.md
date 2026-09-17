# Remote Audit Routes

## Added

- JSON audit route foundation: `/api/audit`.
- Text export route foundation: `/api/audit/export`.
- Shared `RemoteAuditRouteController` for summaries and entries.

## Purpose

The remote manager can expose a small diagnostics/audit panel so the user can see what happened while the watch HTTP server was running: lists, downloads, uploads, denied writes and failed operations.

## Next

- Wire routes in `SimpleHttpEngine`.
- Add audit panel to `WebManagerPage`.
- Include client address and rate-limit events.
