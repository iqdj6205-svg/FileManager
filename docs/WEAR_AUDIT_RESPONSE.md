# Wear Audit Response

## Addressed

- `Grant files` no longer reuses the media callback position blindly: `RuntimePermissionActions.requestStorage()` now exists and MainActivity calls it explicitly.
- Permission readiness is no longer blocked by a duplicated `storageGranted = mediaImages` check. Basic file visibility now depends on at least one usable storage/media grant.
- Wear permission hub now explains file/media status instead of only showing a misleading missing count.
- Wear permission hub uses shared `WearRotaryList`, so it scrolls on small round displays.
- RemoteSettings now returns through the navigation stack instead of hardcoded `Settings`.
- `ServiceBackedRemoteController` no longer returns the fake `Shown in notification` URL/PIN. It reads `RemoteServerStatusStore` and reports Starting/Stopped when the service has not published a running session yet.
- Main Wear navigation now uses a small back stack rather than a single hardcoded `when` map.
- Home and Files screens now use shared `WearRotaryList` helpers instead of the local rotary list implementation.
- File operation progress now shows a progress screen while an operation is running.
- Rename no longer fires an immediate fake `copy-*` rename from the action sheet. A temporary Rename screen with explicit presets is used until keyboard/voice input is added.
- File list emoji markers were replaced with text markers to avoid missing emoji glyphs on older Wear builds.

## Still pending from audit

- Replace temporary rename presets with real text input / RemoteInput / phone-assisted input.
- Wire remote settings into the real `RemoteConfig` used by `RemoteServerService`.
- Split `MainActivity` into route/screen files and remove dead duplicate screens.
- Migrate all Wear media/player/image screens to shared `WearRotaryList`.
- Improve file details for very long paths.
- Replace in-memory favorites/settings with persistent storage.
- Harden Wear channel transfer target path and size limits.
- Replace placeholder Theme/Haptics/Diagnostics settings with real persisted settings.
