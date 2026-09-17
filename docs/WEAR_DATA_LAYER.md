# Wear Data Layer Bridge

## Product rule

The watch app remains fully standalone. The phone app is optional and can send convenience commands, synchronize settings, and transfer files.

## Added module

`core-wear-bridge` contains shared command paths and protocol models for phone-watch communication.

## Planned command flow

Phone → Watch:

- `StartWatchServer`
- `StopWatchServer`
- `GetWatchServerStatus`
- `SyncSettings`
- `GetStorageStatus`
- `SendFileToWatch`
- `RequestFileFromWatch`

Watch → Phone:

- command result
- remote server status
- storage status

## Transport

Use Google Play Services Wearable Data Layer:

- `MessageClient` for commands
- `DataClient` for persistent settings/status snapshots
- `ChannelClient` for file transfer streams

## Requirements

- Phone and Wear apps signed with the same certificate.
- Capabilities declared on both sides.
- Companion features must be optional; no watch feature should require the phone app.
