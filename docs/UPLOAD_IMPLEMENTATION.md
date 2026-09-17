# Upload Implementation

## Added

- `UploadLimits`
- `UploadPlan`
- `UploadPlanner`
- filename safety checks
- target path validation

## Remaining

- Parse multipart/form-data bodies.
- Enforce max upload size while streaming.
- Write to temporary file first.
- Rename temp file only after successful upload.
- Add audit log events.
- Add UI toggle for allowing uploads during a remote session.
