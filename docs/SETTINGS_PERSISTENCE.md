# Settings Persistence Foundation

## Added

- Shared `AppSettings` model.
- Shared `AppThemeMode` enum.
- `AppSettingsRepository` interface.
- `InMemoryAppSettingsRepository` for tests/preview wiring.
- `DataStoreAppSettingsRepository` for real persisted settings.
- `AppSettingsUseCase`.
- Phone settings screen foundation.
- Wear settings state foundation.
- Phone ViewModel factory now injects the DataStore-backed settings use case.

## Next

- Wire phone ViewModel settings toggles to shared settings use case.
- Add DataStore-backed settings use case to Wear ViewModel.
- Sync selected settings through Wear Data Layer.
- Apply settings to file browser filters, remote server config, haptics and theme.
