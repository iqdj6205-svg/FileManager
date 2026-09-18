# Alpha Stabilization Plan

## Goal

Turn the current pre-alpha prototype into a small but real alpha by stabilizing complete vertical flows instead of adding more disconnected foundations.

## Alpha target: Wear standalone

Required before calling the watch app alpha-ready:

1. App launches reliably on a watch/emulator.
2. Permissions are checked on startup and resume.
3. Permission request UI updates immediately after grants/denials.
4. File browser opens practical roots: internal/shared storage, Downloads, Pictures, Music, Movies where available.
5. Folder navigation works with Back, Up, Home and rotary input.
6. File details show name, path, size, type and modified time.
7. Delete works with confirmation and clear error messages.
8. Rename uses real user input, not generated placeholder names.
9. Copy/move has a clear destination picker and safe conflict behavior.
10. Remote server minimum works: start, stop, show URL/PIN, list files, download files.
11. Remote delete/upload stay disabled by default and clearly marked experimental.
12. Main screens are round-watch friendly and not overcrowded.

## Alpha target: Phone app

Required before calling the phone app alpha-ready:

1. Phone file browsing works for common public folders.
2. SAF tree selection, listing and basic operations work with clear errors.
3. Rename/delete/create folder/copy/move work in simple cases.
4. Media list and image preview are usable.
5. Watch companion can find the watch, start/stop watch server and show URL/PIN.
6. Phone can send one file to the watch with visible progress/result.

## Alpha target: Remote server

Minimum alpha scope:

1. Foreground service starts safely.
2. UI shows local network status, URL, port and PIN.
3. `/api/status` returns accurate status.
4. `/api/list` lists a permitted directory.
5. `/api/download` streams a selected file.
6. PIN is consistent across notification, dashboard, Data Layer and web page.
7. Server stops cleanly without socket crash.
8. Upload/delete/rename are either stable or hidden behind explicit experimental mode.

## Do not prioritize yet

These are valuable later, but should not block the first real alpha:

- FTP/WebDAV.
- Full duplicate finder.
- Advanced media playlists.
- Full recursive SAF directory copy.
- Rich audit UI.
- Complex theming.
- Root-only/system folder management.

## Immediate work order

1. Correct documentation and remove misleading percent readiness.
2. Add a smoke-test checklist.
3. Stabilize Wear permissions and storage roots.
4. Stabilize Wear browser navigation and file details.
5. Implement real Wear rename input.
6. Implement copy/move destination picker.
7. Stabilize remote status/list/download only.
8. Re-run build and manual smoke tests.
