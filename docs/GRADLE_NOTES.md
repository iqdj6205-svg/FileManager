# Gradle Notes

This repository includes lightweight `gradlew` launcher scripts that call an installed Gradle command. Android Studio can regenerate the official Gradle Wrapper files if needed.

Recommended local setup:

1. Open the project in Android Studio.
2. Let Android Studio install the required Gradle/Android plugin components.
3. If needed, run `gradle wrapper` once locally to create the official wrapper JAR and properties.
4. Run `./gradlew build` or `gradlew.bat build`.
