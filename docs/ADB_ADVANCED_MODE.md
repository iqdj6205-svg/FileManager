# ADB Advanced Mode

Advanced mode is for users who understand Android storage restrictions.

## Important truth

A normal Wear OS application cannot magically access every protected system folder. Access depends on Android permissions, app sandboxing, user-selected folders, device policy, debug access, and sometimes root.

## Supported approach

The app should provide clear guidance instead of unsafe promises:

1. Use normal media/document permissions for normal users.
2. Use Storage Access Framework where available.
3. Use ADB push/pull workflows for advanced users.
4. Explain risks before destructive commands.

## Example ADB commands

```bash
adb devices
adb shell ls /sdcard
adb pull /sdcard/Download ./watch-download
adb push ./file.txt /sdcard/Download/file.txt
```

## Safety rules

- Do not run random commands from the internet.
- Do not delete system directories.
- Keep backups of important files.
- Treat root access as unsupported advanced behavior.
