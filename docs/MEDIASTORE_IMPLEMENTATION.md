# MediaStore Implementation

A first Android `MediaStoreRepository` draft has been added.

## What it does

- Queries images
- Queries audio
- Queries video
- Maps results into shared `MediaItem` models
- Limits each query to 300 items to avoid heavy watch scans

## Notes

`MediaStore.MediaColumns.DATA` may be unavailable or restricted on newer Android versions. Later implementations should prefer content URIs for opening media and use file paths only when available.
