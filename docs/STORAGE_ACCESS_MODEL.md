# Storage Access Model

## Added

- Shared `StorageRoot` model.
- Shared root types for app-private, shared storage, MediaStore, SAF tree and ADB advanced mode.
- `StorageAccessManager` foundation.
- Phone storage roots screen.
- Wear storage roots screen optimized for round displays.

## Direction

The app should not assume one root path. It needs a root selector:

- normal media/library access for most users;
- common shared folders like Downloads/Documents/Pictures/Music/Videos;
- SAF folder access on phone;
- ADB advanced mode guidance for broader watch access where the OS permits it.

## Remaining

- Wire root state into ViewModels.
- Add Android document tree picker on phone.
- Add permission-aware filtering per root.
- Persist selected root.
- Use selected root for remote server start path and transfers.
