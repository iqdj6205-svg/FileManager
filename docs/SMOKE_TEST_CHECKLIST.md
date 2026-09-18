# Smoke Test Checklist

Use this checkpoint before deeper core refactoring. The goal is to verify that the latest stabilization commits build and that the main started flows do not regress.

## 1. Build

- [ ] Run `./gradlew build` or on Windows `./gradlew.bat build`.
- [ ] Wear debug/release APK installs.
- [ ] Phone debug/release APK installs.
- [ ] If build fails, capture the first Kotlin/Java error block, not the whole log.

## 2. Wear startup/navigation

- [ ] App opens without crash.
- [ ] Main menu is visible on a round screen.
- [ ] Rotary/bezel scroll works on Home, Files, Permissions, Media lists, Playback and Image preview.
- [ ] Hardware/software Back returns to the previous screen instead of unexpectedly exiting sub-screens.

## 3. Wear permissions/storage

- [ ] Permission screen shows actual current state after launch.
- [ ] Grant media opens the Android media permission request.
- [ ] Grant files opens the file/storage permission request path, not the media request.
- [ ] Previously granted permissions are detected after app restart/resume.
- [ ] Permission copy explains that media permission does not unlock protected/system folders.
- [ ] ADB guide shows concrete commands and warns that ADB is optional/advanced.
- [ ] Storage roots screen opens built-in folders where Android allows access.

## 4. Wear files

- [ ] Files screen opens a valid selected/default root.
- [ ] Folder opens on tap.
- [ ] Up button works.
- [ ] File action sheet opens for a file.
- [ ] Details screen shows useful metadata.
- [ ] Delete requires confirmation.
- [ ] Rename screen opens editable input and Save does not run for blank/unchanged value.
- [ ] Copy/move flow shows queued clipboard and paste result.

## 5. Wear remote server

- [ ] Server refuses to start when there is no network.
- [ ] With local-only enabled, server starts only when Wi‑Fi/local IPv4 is available.
- [ ] Dashboard/notification shows reachable URL, port and PIN.
- [ ] Browser can open the web manager page from the same local network.
- [ ] Web manager shows active config: PIN, upload, delete/rename, local network mode, auto-stop.
- [ ] `/api/status?pin=<PIN>` works with the exact PIN.
- [ ] `/api/status` or wrong PIN returns `401 Invalid or missing PIN` when PIN is required.
- [ ] `/api/list?path=/sdcard&pin=<PIN>` returns JSON or a clear permission/path error.
- [ ] File download works for a real file.
- [ ] Downloading a directory returns a clear `400` error.
- [ ] Missing file returns `404`.
- [ ] Stop server does not crash and status becomes stopped.

## 6. Phone app

- [ ] App opens without crash.
- [ ] Phone file browser opens common folders.
- [ ] SAF folder can be added and auto-opens after selection.
- [ ] Storage roots show human-readable labels instead of raw `content://` as the main path.
- [ ] Rename/delete/create folder basic operations work where Android allows them.
- [ ] Settings screen uses section cards and theme selector works.
- [ ] Media screen asks for/grants permissions and loads items.
- [ ] Media bucket chips filter the list.
- [ ] Media preview shows type card/details.
- [ ] Image preview opens and handles empty URI safely.

## 7. Phone ↔ Watch

- [ ] Phone finds paired watch.
- [ ] Phone can request watch server status.
- [ ] Phone can start/stop watch server.
- [ ] Phone shows latest watch URL/PIN.
- [ ] Phone can send one small file to watch using the editable target path.
- [ ] Transfer progress/result is visible.

## Known pre-alpha limitations for this checkpoint

- Phone Remote Manager is still not a full in-app remote file manager; browser/web manager flow is the current practical path.
- Favorites are still not persisted.
- Phone media preview does not yet render real video thumbnails.
- Wear settings persistence is still limited by the current store implementation.
- Core audit is still pending, so deeper cleanup of duplicated/dead classes is intentionally not started yet.
