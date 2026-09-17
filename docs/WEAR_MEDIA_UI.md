# Wear Media UI

## Added

- Wear media state model.
- Wear media controller foundation.
- Round-screen friendly media list and preview composables.

## Purpose

Media browsing on the watch should not depend on the current file-browser folder only. It needs its own MediaStore-backed library and preview/player flows.

## Next

- Inject AndroidMediaStoreRepository into Wear ViewModel.
- Load media categories through MediaStore.
- Add real image preview and Media3 player controls.
- Add rotary support to the media list composables.
