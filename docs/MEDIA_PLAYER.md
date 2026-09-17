# Media Player Foundation

## Added

- Shared playback state models.
- Shared playback command models.
- `MediaPlaybackController` interface.
- In-memory preview controller for tests/preview wiring.
- Phone Android Media3 playback controller using ExoPlayer.
- Wear Android Media3 playback controller using ExoPlayer.
- Wear round-screen playback controls.
- Phone playback controls.
- Phone ViewModel factory now uses the Media3 controller.

## Next

- Wire Wear ViewModel to MediaStore-backed media and Media3 playback.
- Add image rendering path for image files.
- Add audio/video foreground playback behavior.
- Add headset/Bluetooth controls where available.
- Add battery-aware playback and thumbnail policies on Wear OS.
