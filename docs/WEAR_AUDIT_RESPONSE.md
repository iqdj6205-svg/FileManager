# Wear Audit Response

## Addressed immediately

- `Grant files` no longer reuses the same callback position as an unnamed media action: `RuntimePermissionActions.requestStorage()` now exists and MainActivity calls it explicitly.
- Permission readiness is no longer blocked by a duplicated `storageGranted = mediaImages` check. Basic file visibility now depends on at least one usable storage/media grant.
- Wear permission hub now explains file/media status instead of only showing a misleading missing count.
- RemoteSettings back navigation now remembers whether the screen was opened from Remote or Settings.
- `ServiceBackedRemoteController` no longer returns the fake `Shown in notification` URL/PIN. It reads `RemoteServerStatusStore` and reports Starting/Stopped when the service has not published a running session yet.

## Still pending from audit

- Replace mutable single-screen navigation with a real navigation stack.
- Replace placeholder rename with real text input / RemoteInput flow.
- Wire remote settings into the real `RemoteConfig` used by `RemoteServerService`.
- Split `MainActivity` and remove dead duplicate screens.
- Migrate all Wear media/file screens to shared `WearRotaryList`.
- Add operation progress UI and better file details.
- Replace in-memory favorites/settings with persistent storage.
- Harden Wear channel transfer target path and size limits.
