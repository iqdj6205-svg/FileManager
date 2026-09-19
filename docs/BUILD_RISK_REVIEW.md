# Build Risk Review Checkpoint

## Latest warning cleanup

The following Wear compiler warnings were addressed in commit `pending`:

- `LocalLifecycleOwner` now comes from `androidx.lifecycle.compose` with `lifecycle-runtime-compose`.
- `NetworkStatus` filters nullable interface addresses with `mapNotNull`.
- `RemoteAddressResolver` no longer uses deprecated `WifiManager.connectionInfo.ipAddress`.
- Notification stop action uses `Notification.Action.Builder` instead of deprecated three-argument `addAction`.
- Wear channel target selection no longer checks a value that Kotlin already knows is non-null.

A local `./gradlew.bat build` is still required to confirm the dependency/import change on the user's machine.

## Earlier review

- Wear rename `item` parameter shadowed the lazy-list `item {}` DSL function. Fixed in `06e1bbd`.
- Wear remote config reached lifecycle policy but not the embedded HTTP engine. Fixed in `78c4ace`.
- Embedded server session was not consistently published to the shared status store. Fixed in `1ff1d86`.
- Remote API route policy accepted PIN-shaped values before exact PIN comparison. Fixed in `948de3f`.

## Recommended local command

```powershell
.\gradlew.bat build
```
