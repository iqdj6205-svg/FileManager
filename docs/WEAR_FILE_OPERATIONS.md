# Wear File Operations

## Added

- Wear clipboard state.
- Wear file operation action controller.
- Copy/move clipboard operations.
- Paste into current directory foundation.
- Duplicate file helper using shared name policy.

## Direction

Wear file operations must be usable without the phone:

- select file;
- choose copy or move;
- navigate to destination;
- paste;
- show compact success/failure messages;
- keep all controls rotary-friendly and touch-friendly.

## Remaining

- Wire `WearFileManagerActions` directly into `FileManagerViewModel`.
- Add visible clipboard banner/action in file browser.
- Add real rename text input for Wear using voice/keyboard/companion handoff.
- Add operation progress for large copy/move tasks.
