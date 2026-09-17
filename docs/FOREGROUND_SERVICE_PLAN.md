# Foreground Service Plan

Remote access should run with a visible foreground-service notification.

## Added

- `RemoteServerService`
- notification channel
- ongoing notification
- stop action
- start/stop intents
- `RemoteServiceController`
- manifest `foregroundServiceType="dataSync"`

## Remaining before beta

- Move actual `EmbeddedHttpFileServer` lifecycle from ViewModel into the service.
- Add low-battery auto-stop.
- Add timeout auto-stop.
- Add audit events when service starts/stops.
- Add notification permission explanation on Android 13+.
