# API Hardening Plan

## Implemented

- Route constants
- Basic HTTP responses
- PIN checking foundations
- Path guard for traversal and protected paths
- Directory JSON serializer

## Required before public beta

- Stream downloads without loading whole files into memory
- Multipart upload parser with file size limits
- Method checking: GET/POST separation
- Rate limits / request limits
- Local-network binding and explicit warnings
- Foreground service notification while server is running
- HTTPS is unlikely on local watch server; require local trusted network and PIN
- Audit logs for remote write/delete actions

## Security defaults

- Server off by default
- Upload disabled by default
- Delete disabled by default
- PIN required
- Auto-stop enabled
