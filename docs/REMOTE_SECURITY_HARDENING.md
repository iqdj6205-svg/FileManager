# Remote Security Hardening

## Implemented foundations

- Local HTTP route executor.
- Route validation before read/upload/destructive operations.
- Upload policy and size guard.
- Binary-safe raw request parser.
- Audit sink and audit export routes.
- HTTP request rate limiting by client address.
- Route-policy validation wired into `SimpleHttpEngine`.
- Client address stored in download/upload/denied audit entries.

## Current policy

- PIN is required when `RemoteConfig.requirePin` is enabled.
- Upload and mkdir require `allowUploads`.
- Rename and delete require `allowDelete`.
- Unknown routes are rejected.
- Request bursts return HTTP 429.

## Remaining

- Multipart upload parsing.
- Stronger PIN/session rotation and QR pairing.
- Network interface binding validation for local-only mode.
- Persistent audit export.
- Per-route rate limits for destructive operations.
