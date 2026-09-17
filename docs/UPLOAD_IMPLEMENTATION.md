# Upload Implementation

## Added

- `RemoteUploadWriter` for guarded raw upload persistence.
- Filename sanitization for upload targets.
- Upload size enforcement with cleanup on overflow.
- `HttpRequest` and parser foundation.
- Raw request body parser foundation.

## Notes

The current upload path is a raw body foundation used by the remote web manager's fetch upload. Multipart form parsing is still pending for broader browser compatibility.

## Remaining

- Wire binary-safe input stream handling instead of text reader body fallback.
- Add multipart/form-data parser.
- Add progress reporting and audit entries with client IP.
- Add UI status to remote web manager.
