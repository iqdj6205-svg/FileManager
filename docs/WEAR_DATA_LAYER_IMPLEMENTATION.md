# Wear Data Layer Implementation

## Added

- `core-wear-bridge` shared protocol module.
- Wear-side `WearBridgeListenerService`.
- Phone-side `WearBridgePhoneClient`.
- Dependencies added to both apps.
- Wear manifest listener registration.
- Phone `PhoneViewModelFactory` injects the bridge client.
- Phone Watch Companion UI now sends real Data Layer commands for start/stop/status.

## Current commands wired on watch

- `StartWatchServer` starts `RemoteServerService`.
- `StopWatchServer` stops `RemoteServerService`.
- `GetWatchServerStatus` returns a compact status payload.

## Remaining

- Add DataClient snapshots for settings/status.
- Add capability declarations.
- Add ChannelClient for file transfer.
- Improve status payload with stable serialization.
- Surface returned command results on phone via a listener instead of fire-and-forget status text.
