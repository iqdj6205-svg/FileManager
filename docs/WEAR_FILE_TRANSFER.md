# Wear File Transfer

## Added

- Wear capability declarations for phone and watch.
- Shared transfer request/progress models.
- Shared transfer string codec.
- Phone `WearChannelTransferClient` using `ChannelClient`.
- Watch `WearChannelListenerService` that receives channel data.
- Metadata handshake command `PrepareFileTransfer`.
- Watch pending transfer store.
- Phone transfer controller now sends metadata before opening the channel.
- Watch preserves the original filename when the pending metadata is available.

## Direction

ChannelClient is the right foundation for larger phone-watch file transfers. MessageClient stays for commands; DataClient for status/settings snapshots.

## Remaining

- Add transfer progress updates.
- Add user-selected destination folder on watch.
- Add reverse transfer from watch to phone.
- Move received files from app-private storage to user-selected public storage when permissions allow.
