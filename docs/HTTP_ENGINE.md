# HTTP Engine

A first embedded HTTP engine draft has been added behind `SimpleHttpEngine` and `EmbeddedHttpFileServer`.

## Current prototype endpoints

- `/` returns a simple web manager page.
- `/api/status` returns server status JSON.
- `/api/list?path=/sdcard` returns a simple JSON list of visible files.

## Hardening still required

- PIN checking on protected routes
- URL decoding
- MIME detection
- File download streaming
- Upload multipart parsing
- Request size limits
- Local-network binding checks
- Foreground service integration
- Better error responses
- Auto-stop timer

The prototype is intentionally isolated so the rest of the app can be developed safely while server internals mature.
