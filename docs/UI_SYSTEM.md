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

## Migration plan

1. Add shared components.
2. Migrate Wear Remote and Wear Permissions first.
3. Migrate Wear Files and Home.
4. Migrate Phone Home and Phone Files.
5. Split monolithic `MainActivity` files after common components are stable.
