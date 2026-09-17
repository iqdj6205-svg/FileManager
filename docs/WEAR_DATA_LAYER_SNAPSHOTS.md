# Wear Data Layer Snapshots

## Added

- Shared DataMap keys.
- Remote status DataMap codec.
- Settings DataMap codec.
- Wear-side `WearStatusPublisher`.
- Phone-side remote status store.
- Phone listener now handles DataClient status changes.

## Purpose

Messages are good for commands. Data snapshots are better for current state:

- remote server running/stopped
- URL
- PIN
- network label
- battery percent
- settings snapshot

## Next

- Publish status whenever `RemoteServerService` starts/stops.
- Read status in phone ViewModel and show it in UI.
- Add settings sync from phone to watch.
