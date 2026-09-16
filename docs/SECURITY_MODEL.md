# Security Model

## Remote access security

- Server is off by default
- User must explicitly start server
- Session PIN required
- Local-network-only by default
- Upload/delete actions require explicit enablement
- Auto-stop after timeout
- Stop on low battery
- Show clear active-server status

## Storage safety

- Use official Android APIs
- Request media permissions only when needed
- Avoid misleading claims about protected folders
- Explain ADB/root limitations in advanced mode

## Dangerous operations

- Ask for confirmation in app UI
- Show target path
- Prefer undo/trash where possible
- Never allow unauthenticated remote destructive actions
