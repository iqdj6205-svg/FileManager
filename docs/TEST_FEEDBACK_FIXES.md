# Test Feedback Fixes

## Feedback from first Wear OS run

- File and media sections are empty without an obvious permission request.
- Server start/stop works, but stopping could crash with `Socket closed` in the accept loop.
- Back from Remote screen exited the app instead of returning home.
- Bezel/rotary input did not scroll the main menu.

## Fixes added

- Added visible `Grant access` action on Files screen.
- Added visible `Grant media` action on Media screen.
- Added `BackHandler` so system back returns to the previous app screen instead of exiting from nested screens.
- Added a `RotaryScalingLazyColumn` wrapper that focuses lists and handles rotary scroll events.
- Hardened `SimpleHttpEngine.stop()` and accept loop so closing the server socket during shutdown is treated as normal, not a fatal coroutine crash.
- Fixed compile error: `ScalingLazyListState` in current Wear Compose does not expose `scrollBy`; rotary now steps with `scrollToItem(centerItemIndex +/- 1)`.

## Retest checklist

1. Pull latest changes.
2. Build and run `wear-app`.
3. Open Files and tap `Grant access`.
4. Open Media and tap `Grant media`.
5. Start and stop Remote server several times; app should not crash.
6. Press system Back on Remote screen; it should return to Home.
7. Try bezel/rotary scrolling on Home and list screens.
