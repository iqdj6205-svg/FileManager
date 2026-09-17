# Media Session

## Added

- Shared media session metadata model.
- Shared media notification state model.
- Mapper from playback session to notification-friendly state.
- Phone/Wear state wrappers for UI and future Android notification/session integration.

## Purpose

Audio and video playback should behave like a normal Android media app:

- stable play/pause state;
- lock screen / notification controls on phone;
- compact controls on Wear OS;
- headset/Bluetooth command compatibility;
- safe lifecycle cleanup when leaving playback.

## Remaining

- Wire AndroidX Media3 `MediaSession` objects.
- Add foreground service for long audio playback.
- Add notification permission check before showing playback notification.
- Add headset/Bluetooth media button handling.
