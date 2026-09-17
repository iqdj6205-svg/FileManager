# ADB Advanced Mode

ADB mode is for advanced users who want broader file visibility on their own watch.

## Commands

```bash
adb devices
adb shell appops set com.sere.filemanager.wear MANAGE_EXTERNAL_STORAGE allow
adb shell pm grant com.sere.filemanager.wear android.permission.READ_EXTERNAL_STORAGE
adb shell pm grant com.sere.filemanager.wear android.permission.READ_MEDIA_IMAGES
adb shell pm grant com.sere.filemanager.wear android.permission.READ_MEDIA_VIDEO
adb shell pm grant com.sere.filemanager.wear android.permission.READ_MEDIA_AUDIO
adb shell am force-stop com.sere.filemanager.wear
```

Then start FileManager again on the watch.

## Notes

- Some Wear OS builds restrict broad storage access regardless of ADB commands.
- Android 13+ uses separate media permissions.
- ADB should not be required for normal media browsing.
- The watch app must remain fully usable without the phone companion.
