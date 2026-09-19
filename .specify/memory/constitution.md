# FileManager Constitution

## Core Principles

### I. Pre-alpha Honesty
Project is `pre-alpha` — not alpha/beta. No percent readiness. `docs/REAL_STATUS.md` and `docs/PROGRESS.md` reflect actual state. Audits are `needs verification` until smoke-tested on device/emulator.

### II. Build Must Be Green (NON-NEGOTIABLE)
`.\gradlew.bat --no-daemon build` (or `assembleDebug` for quick check) must be `BUILD SUCCESSFUL` before any commit. `gradle` calls use explicit `timeout`, `--no-daemon`, no `Select-Object` piping. Imports via `import`, never full qualified addresses. `R8` shrink deferred — focus on correctness first.

### III. Wear Standalone First
Watch `wear-app` (`minSdk 30`) works without phone: browsing, permissions, operations, media, remote server. Phone `phone-app` (`minSdk 26`) is companion — optional. Same `core-*` modules, separate `com.sere.filemanager.wear` / `phone`.

### IV. Unified Design System
Single tokens: `core-ui/DesignTokens.kt` (`PhonePadding 24dp`, `WatchEdgePadding 18dp` etc) + `wear/ui/WearUi.kt` (`WearRotaryList`, `wearTitle`, `wearPrimaryAction`) + `phone/ui/PhoneUi.kt` (`PhoneScreenScaffold`, `PhoneSectionCard`). New screens use tokens, existing screens migrated incrementally. No hardcoded `24.dp` duplicates, no emoji as icons.

### V. Check — Fix — Commit — Test
Verification loop: smoke-test checklist `docs/SMOKE_TEST_CHECKLIST.md` → fix small batch → `git commit` → `BUILD SUCCESSFUL` → device test. No large blind fixes. `docs/AUDIT_TRACKER.md` tracks `found → fixed → verified`.

## Additional Constraints

- Stack: Kotlin 2.0.20, Compose 1.7.4 / Wear Compose 1.4.0 / Material3 1.3.0, AGP 8.6.1, Java 17, `targetSdk 35`.
- Security: Remote server off by default, local-only, PIN `requirePin`, `allowUploads`/`allowDelete` flags, foreground `dataSync`.
- Performance: `wear-app` dex uncompressed when `minSdk >=28` (`DexPackaging.useLegacyPackaging=null`) — intentional for mmap, size addressed via R8 later, not legacy compression.

## Development Workflow

- Spec-kit: `spec.md` → `plan.md` → `tasks.md` per feature, sequential numbering `specs/###-feature`.
- Reviews verify `constitution` compliance, `BUILD_RISK_REVIEW.md` for risky areas.
- Smoke tests before calling any flow `alpha-ready` (see `ALPHA_STABILIZATION_PLAN.md`).

## Governance

Constitution supersedes all other practices. Amendments require doc update, approval, migration plan. All PRs must verify `BUILD SUCCESSFUL` and smoke-test.

**Version**: 1.0.0 | **Ratified**: 2026-09-19 | **Last Amended**: 2026-09-19
