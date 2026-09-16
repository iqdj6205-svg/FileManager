# Remote Audit Log

Remote access should record important actions so users can understand what happened during a session.

## Events

- Server started
- Server stopped
- Directory listed
- Download requested
- Upload requested
- Delete requested
- Error

## Current state

`RemoteAuditLog` and an in-memory implementation exist. Before beta this should be connected to the foreground service and optionally exported through diagnostics.
