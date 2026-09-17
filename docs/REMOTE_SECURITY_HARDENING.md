# Remote Security Hardening

## Added

- Request validator foundation.
- Route policy resolver.
- Read/upload/destructive operation validation split.
- Upload policy with default size and extension blocks.
- Audit entry model and in-memory audit sink.
- Audit summary model.
- JSON audit exporter.
- Status JSON renderer with optional audit and health sections.
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
