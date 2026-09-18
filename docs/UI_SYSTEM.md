# UI System

## Why this exists

The app has many screens. Each screen must not invent its own spacing, buttons, list behavior, back behavior, loading state and error style.

The UI should work like imported shared building blocks: screens compose common components instead of redefining design rules every time.

## Shared core

`core-ui` owns cross-platform tokens and common labels:

- `DesignTokens`
- `FileManagerText`
- `FileManagerTheme`

## Wear rules

Wear screens should use:

- `WearRotaryList` for scrollable round-screen lists.
- `wearTitle` for title/subtitle.
- `wearPrimaryAction` / `wearSecondaryAction` for actions.
- `wearLoading`, `wearEmpty`, `wearError` for state display.
- `wearBackAction` for predictable back actions.

Rules:

1. Rotary scrolling must work on primary Wear screens.
2. Text should be short.
3. Dangerous actions need confirmation.
4. Empty/error/loading states must be visible and consistent.
5. Back behavior should be predictable and not exit the app unexpectedly from nested screens.

## Phone rules

Phone screens should use:

- `PhoneScreenScaffold`
- `PhoneSectionCard`
- `PhoneActionRow`
- `PhoneEmptyState`
- `PhoneErrorState`

Rules:

1. Shared spacing and section cards.
2. Clear primary/secondary action hierarchy.
3. Reusable error and empty states.
4. Avoid one giant screen file when feature screens grow.

## Migration status

- Shared core tokens: added.
- Wear helper components: added.
- Phone helper components: added and compile-safe with `ColumnScope` content.
- Wear Permissions screen: migrated.
- Wear ADB guide: migrated.
- Wear Remote dashboard: migrated.

## Next migration targets

1. Wear Home.
2. Wear Files.
3. Wear Settings.
4. Phone Home.
5. Phone Files.
6. Split large `MainActivity` files after common components are stable.
