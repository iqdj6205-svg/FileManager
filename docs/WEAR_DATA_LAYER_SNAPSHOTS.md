# Wear Data Layer Snapshots

## Added

- Shared DataMap keys.
- Remote status DataMap codec.
- Settings DataMap codec.
- Wear-side `WearStatusPublisher`.
- Phone-side remote status store.
- Phone listener handles DataClient status changes.
- `RemoteServerService` publishes status when server starts, stops, is destroyed, and during periodic timeout checks.
- Phone ViewModel can read the latest remote snapshot and update the UI/server URL.
- Phone Watch Companion UI has a `Read latest status` action.

## Purpose

Messages are good for commands. Data snapshots are better for current state:

- remote server running/stopped
- URL
- PIN
- network label
- battery percent
- settings snapshot

## Next

- Add settings sync from phone to watch.
- Replace polling/status-store with reactive Flow.
- Add capability declarations.
