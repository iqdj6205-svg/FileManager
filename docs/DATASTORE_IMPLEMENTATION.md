# DataStore Implementation

A first `DataStoreSettingsRepository` has been added in `core-files`.

## Persisted settings

- dark theme
- haptics
- advanced mode
- show hidden files
- confirm deletes
- remote auto-stop minutes

## Next wiring step

The Wear and phone apps can inject this repository later through a simple factory or DI framework. For now, the ViewModels keep in-memory defaults so previews and early app startup stay simple.
