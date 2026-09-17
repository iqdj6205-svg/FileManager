# Settings Persistence Foundation

## Added

- Shared `AppSettings` model.
- Shared `AppThemeMode` enum.
- `AppSettingsRepository` interface.
- `InMemoryAppSettingsRepository` for UI wiring before DataStore replacement.
- `AppSettingsUseCase`.
- Phone settings screen foundation.
- Wear settings state foundation.

## Next

- Replace in-memory repository with DataStore-backed implementation.
- Wire phone and watch settings screens to shared settings use case.
- Sync selected settings through Wear Data Layer.
- Apply settings to file browser filters, remote server config, haptics and theme.
