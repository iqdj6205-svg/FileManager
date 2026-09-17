# Remote Security Hardening

## Added

- Request validator foundation.
- Read/upload/destructive operation validation split.
- Audit entry model and in-memory audit sink.
- JSON audit exporter.
- Basic per-client rate limiter.

## Security direction

Remote access must be convenient but safe by default:

- local network only by default;
- PIN enabled by default;
- uploads disabled by default;
- delete/rename disabled by default;
- audit trail for write/destructive operations;
- rate limits for repeated requests.

## Remaining

- Wire validator into HTTP route handling.
- Include real client IP in audit records.
- Persist audit logs if user enables diagnostics.
- Add request-size limits for upload bodies.
- Add UI toggles explaining destructive permissions clearly.
