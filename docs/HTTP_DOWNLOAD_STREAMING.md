# HTTP Download Streaming

The remote prototype now has a binary-capable HTTP response model.

## Added

- `HttpResponse`
- `HttpResponseWriter`
- `HttpResponseFactory`
- `DownloadPlanner`
- Real file streaming response for `/api/download`

## Current download endpoint

```text
GET /api/download?path=/sdcard/Download/file.txt&pin=123456
```

## Remaining upload work

Upload still needs multipart parsing, size limits, and explicit user enablement from the watch UI.
