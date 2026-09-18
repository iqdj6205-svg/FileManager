# Project Progress

## Current honest status

The project is **pre-alpha**.

Previous percentage-style progress values were misleading because they measured how many foundations existed, not how many product flows were stable. Percent readiness should not be used until the app has reliable alpha criteria and smoke tests.

## Stabilization updates

- Wear permission state is now read through `PermissionStateReader` at ViewModel startup/resume.
- Shared UI system foundations added for Wear and phone screens.
- Wear Permission, ADB guide and Remote dashboard screens now use shared Wear UI components.
- Wear storage root state is now part of app state.
- Wear storage roots can be selected from Home/Settings and open the selected practical folder.

## Current focus

1. Stabilize complete vertical flows.
2. Use shared UI components instead of per-screen custom styling.
3. Make Wear standalone file browsing and remote status/download actually reliable.

## Next stabilization block

- Migrate Wear Home/Files fully to shared UI components.
- Real file details and rename input flow.
- Remote server status/list/download consistency.
