# Wear Data Layer Implementation

## Added

- `core-wear-bridge` shared protocol module.
- Wear-side `WearBridgeListenerService`.
- Phone-side `WearBridgePhoneClient`.
- Dependencies added to both apps.
- Wear manifest listener registration.

## Current commands wired on watch

- `StartWatchServer` starts `RemoteServerService`.
- `StopWatchServer` stops `RemoteServerService`.
- `GetWatchServerStatus` returns a compact status payload.

## Remaining

- Phone UI needs to call `WearBridgePhoneClient` instead of placeholder command creation.
- Add DataClient snapshots for settings/status.
- Add capability declarations.
- Add ChannelClient for file transfer.
- Improve status payload with stable serialization.
