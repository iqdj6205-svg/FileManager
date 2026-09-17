# Remote Service Implementation

## Added

- Battery monitor
- Remote lifecycle policy
- Foreground notification with URL/PIN text
- Timeout check loop
- Actual embedded HTTP server lifecycle moved into `RemoteServerService`
- Stop action shuts down server and service

## Notes

The ViewModel still has an in-memory server controller for UI state, but production remote serving now has a real service path. The next step is to have the UI start/stop the service and expose service state back to the UI.
