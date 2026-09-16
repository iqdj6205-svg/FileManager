# Permissions Implementation

## Wear OS permissions

The app separates permission calculation from UI:

- `PermissionHelper` returns the correct permission list based on Android version.
- `WearPermissionManager` reads current permission state.
- `RuntimePermissionActions` launches runtime requests from an Activity Result launcher.

## Permission groups

Android 13+:

- `READ_MEDIA_IMAGES`
- `READ_MEDIA_VIDEO`
- `READ_MEDIA_AUDIO`
- `POST_NOTIFICATIONS`

Android 12 and lower:

- `READ_EXTERNAL_STORAGE` with max SDK behavior

## Product rule

The app must explain that media permission is not the same as full file-system access. Protected paths require ADB/root/device-owner workflows and are advanced-only.
