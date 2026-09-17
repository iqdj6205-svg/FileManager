# Wear File Transfer

## Added

- Wear capability declarations for phone and watch.
- Shared transfer request/progress models.
- Shared transfer string codec.
- Phone `WearChannelTransferClient` using `ChannelClient`.
- Watch `WearChannelListenerService` that receives channel data into app-private storage.

## Direction

ChannelClient is the right foundation for larger phone-watch file transfers. MessageClient should stay for commands; DataClient for status/settings snapshots.

## Remaining

- Add file picker integration on phone.
- Send transfer metadata before opening channel.
- Preserve original filenames on watch.
- Add progress updates.
- Add user-selected destination folder on watch.
- Add reverse transfer from watch to phone.
