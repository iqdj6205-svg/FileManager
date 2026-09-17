# Remote Route Execution

## Added

- `RemoteRouteExecutor` service for list/status/mkdir/rename/delete/upload validation.
- Route validation before reads, uploads and destructive operations.
- Audit records for status, list, mkdir, rename, delete and upload validation.
- `RemoteExecutionResult` for HTTP-friendly route results.
- `RemoteRouteQuery` helper for URL query parsing.

## Next

- Wire executor directly into `EmbeddedHttpFileServer`/`SimpleHttpEngine` routes.
- Return correct HTTP status codes from route failures.
- Stream downloads and uploads through guarded paths.
- Include client address in audit records.
