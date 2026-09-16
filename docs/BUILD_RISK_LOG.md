# Build Risk Log

## Known risks before first Android Studio sync

1. Dependency versions may need alignment with the installed Android Gradle Plugin and Compose compiler.
2. Lightweight Gradle wrapper scripts call installed Gradle; Android Studio can regenerate official wrapper files.
3. Wear Compose APIs may require minor import/signature adjustments after sync.
4. Embedded HTTP server uses Java networking APIs that should be checked against Android/Wear OS behavior.
5. Runtime permission launcher wiring still needs Activity-level integration.

## Mitigation

- Keep architecture modular.
- Keep platform-specific code isolated.
- Prefer small commits.
- Use GitHub Actions and local Gradle logs to fix compile issues after first run.
