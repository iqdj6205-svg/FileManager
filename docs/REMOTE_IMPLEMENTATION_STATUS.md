# Remote Implementation Status

## Added

- Session manager
- Audit events
- Download planner
- MIME type detection
- Embedded HTTP controller factory
- PIN-protected list route
- Path guard

## Current limitation

The HTTP engine can serve metadata and route responses, but true binary download streaming and multipart uploads still need a lower-level response API instead of string-only responses.

## Next

- Introduce binary HTTP responses.
- Add file streaming.
- Add upload route skeleton.
- Move server lifecycle into `RemoteServerService`.
