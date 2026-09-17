# Wear Data Layer Implementation

## Added

- `core-wear-bridge` shared protocol module.
- Wear-side `WearBridgeListenerService`.
- Phone-side `WearBridgePhoneClient`.
- Phone-side `PhoneBridgeListenerService`.
- Phone `PhoneBridgeStatusStore` for latest command result.
- Dependencies added to both apps.
- Wear and phone manifest listener registration.
- Phone `PhoneViewModelFactory` injects the bridge client.
- Phone Watch Companion UI now sends real Data Layer commands for start/stop/status.
- Phone can query connected watch names.
- Phone can display latest command result returned by the watch.

## Current commands wired on watch

- `StartWatchServer` starts `RemoteServerService`.
- `StopWatchServer` stops `RemoteServerService`.
- `GetWatchServerStatus` returns a compact status payload.

## Remaining

- Add DataClient snapshots for settings/status.
- Add capability declarations.
- Add ChannelClient for file transfer.
- Improve status payload with stable serialization.
- Replace delayed latest-result polling with reactive state.
