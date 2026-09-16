# Persistence Plan

## Settings

First implementation uses in-memory repositories so the app structure compiles and the UI can be wired. Production implementation should use DataStore Preferences.

Settings to persist:

- advanced mode
- show hidden files
- confirm deletes
- battery-safe mode
- remote server timeout
- haptics
- theme

## Favorites and recents

First implementation uses in-memory repositories. Production implementation should persist favorites and recent paths with DataStore or Room if metadata becomes complex.

## Why not SharedPreferences?

DataStore is preferred for modern Kotlin apps because it supports coroutines and safer async updates.
