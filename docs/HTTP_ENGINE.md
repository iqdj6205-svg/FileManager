# HTTP Engine

A first embedded HTTP engine draft has been added behind `SimpleHttpEngine` and `EmbeddedHttpFileServer`.

## Current prototype endpoints

- `/` returns a simple web manager page.
- `/api/status` returns server status JSON.
- `/api/list?path=/sdcard&pin=123456` returns visible files.
- `/api/download?path=/sdcard/file.txt&pin=123456` is reserved for download streaming.

## Implemented foundations

- Route constants
- Route policies
- Session PIN generation
- Basic query parsing
- Basic JSON list response
- HTTP response helpers
- Server lifecycle API

## Hardening still required

- File download streaming
- Upload multipart parsing
- Request size limits
- Local-network binding checks
- Foreground service integration
- Better error responses
- Auto-stop timer
- Path traversal protection

The prototype is intentionally isolated so the rest of the app can be developed safely while server internals mature.
