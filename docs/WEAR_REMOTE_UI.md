# Wear Remote UI

## Added

- Remote dashboard foundation for watch.
- Direct status display: state, URL, PIN, network label and battery percent.
- Remote settings screen foundation.
- Remote settings controller backed by `WearSettingsStore`.

## Purpose

The server screen should not hide important connection details in notifications only. The watch must show the address and PIN directly when available.

## Remaining

- Wire dashboard into existing Wear navigation.
- Use settings values when creating `RemoteConfig`.
- Persist settings with DataStore.
- Hide permission actions once permission is granted.
