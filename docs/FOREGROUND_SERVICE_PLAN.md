# Foreground Service Plan

Remote access should eventually run in a foreground service, not directly in UI state.

## Why

- Android may stop background networking.
- Users need a persistent visible indicator while remote access is active.
- Server lifetime should survive short UI navigation but stop safely.

## Required before beta

- Notification channel
- Persistent notification with stop action
- Start/stop intents
- Low battery auto-stop
- Timeout auto-stop
- Audit log entry when server starts/stops

## Current state

`RemoteServerService` exists as a placeholder. The prototype server lifecycle is still driven from the ViewModel.
