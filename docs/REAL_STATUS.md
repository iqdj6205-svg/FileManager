# Real Project Status

Last reviewed: 2026-09-18

## Honest status

FileManager is currently a **pre-alpha prototype**, not an alpha-ready product.

The repository contains a useful Android/Wear OS architecture skeleton and many feature foundations, but most user-facing flows are incomplete, fragile, or only partially connected.

## Readiness summary

| Area | Status | What works now | Main gaps |
|---|---|---|---|
| Build/project structure | Partially stable | Multi-module Android project, release build was possible after local fixes | Needs repeatable CI/build validation after each larger change |
| Wear file browser | Prototype | Basic path listing, folder navigation, simple actions | Permission state, destination picker, true rename input, persistence, robust errors |
| Wear file operations | Prototype | Basic delete/copy/move foundations | No polished UX, no reliable progress, weak conflict handling, no full storage model |
| Wear remote server | Prototype | Service start/stop, HTTP skeleton, status/list/download foundations | Settings not fully applied, upload/multipart incomplete, custom HTTP engine fragile |
| Wear media | Demo/prototype | MediaStore loading, image preview, basic Media3 playback | No real MediaSession/notification service, no audio focus, no playlist/queue |
| Phone file manager | Prototype | Basic browser, SAF picker, simple operations, progress card | No full UX, search/sort/multi-select/share/open-with incomplete |
| Phone companion | Prototype | Wear commands/status/file-send foundations | Pairing UX, destination selection, reverse transfer, error handling incomplete |
| Settings | Prototype | DataStore foundations on phone, UI models | Not consistently applied to real runtime behavior |
| Documentation | Needs correction | Many design docs exist | Progress docs overstated readiness |

## Current product readiness

- Product readiness: **pre-alpha**
- Architecture skeleton readiness: **medium/high**
- Stable end-user feature readiness: **low**
- Recommended testing label: **internal prototype only**

## Stabilization principle

Do not add broad new foundations until existing vertical flows are made real.

Each feature should be treated as one of:

- **Ready for alpha** — works end-to-end with clear UX and errors.
- **Prototype** — usable only in simple cases.
- **Experimental** — visible only if explicitly enabled or documented as risky.
- **Foundation only** — code exists but should not be presented as a finished feature.
- **Remove/rewrite** — misleading or brittle implementation.
