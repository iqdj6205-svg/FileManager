# Smoke Test Checklist

Use this after each stabilization block.

## Build

- [ ] `./gradlew build` succeeds.
- [ ] Wear debug/release APK installs.
- [ ] Phone debug/release APK installs.

## Wear startup

- [ ] App opens without crash.
- [ ] Main menu is visible on round screen.
- [ ] Rotary/bezel scroll works on main menu.
- [ ] Back exits sub-screens predictably, not the app unexpectedly.

## Wear permissions

- [ ] Permission screen shows actual current state.
- [ ] Granting media/storage permissions updates UI.
- [ ] Previously granted permissions are detected after app restart.
- [ ] Denied permissions show clear explanation.

## Wear files

- [ ] Files screen opens a valid root.
- [ ] Folder opens on tap.
- [ ] Up button works.
- [ ] File action sheet opens for a file.
- [ ] Details screen shows useful metadata.
- [ ] Delete requires confirmation.
- [ ] Rename uses real input.
- [ ] Copy/move flow shows destination and result.

## Wear remote server

- [ ] Start server succeeds only when network/battery policy allows it.
- [ ] Dashboard shows URL, port and PIN.
- [ ] Phone/browser can open the web page.
- [ ] `/api/status` works.
- [ ] `/api/list` works with PIN.
- [ ] File download works.
- [ ] Stop server does not crash.

## Phone app

- [ ] App opens without crash.
- [ ] Phone file browser opens common folders.
- [ ] SAF folder can be added and listed.
- [ ] Rename/delete/create folder basic operations work.
- [ ] Media screen asks for/grants permissions and loads items.
- [ ] Image preview opens.

## Phone ↔ Watch

- [ ] Phone finds paired watch.
- [ ] Phone can request watch server status.
- [ ] Phone can start/stop watch server.
- [ ] Phone shows latest watch URL/PIN.
- [ ] Phone can send one small file to watch.
- [ ] Transfer result is visible on both sides.
