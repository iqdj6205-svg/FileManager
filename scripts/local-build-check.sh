#!/usr/bin/env bash
set -euo pipefail
echo "FileManager local build check"
echo "1. Ensure Android Studio / Gradle is installed"
echo "2. Running Gradle build..."
./gradlew build
