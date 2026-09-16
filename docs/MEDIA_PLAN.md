# Media Plan

## Gallery

- Build from accessible file locations first.
- Add MediaStore integration later for broader Android media indexing.
- Use lazy thumbnails and avoid background scanning on low battery.

## Audio

- Use Media3 for playback.
- Keep watch controls minimal: play/pause, next, previous, progress.
- Add phone companion controls later.

## Video

- Start with preview/open-with.
- Add Media3 playback once file access and battery policies are stable.

## Battery rules

- Generate thumbnails lazily.
- Cache thumbnails conservatively.
- Pause expensive scans below configured battery threshold.
