# TreadLog (Native Android Edition)

This is a lightning-fast, privacy-first, offline-capable mobile application tailored specifically for treadmill owners. It was originally a hybrid web application but has been fully converted to a Native Android app using Kotlin and Jetpack Compose.

## 1. Run & Build Locally
The app is purely native Android now. You do not need Node.js or `npm`.

**Prerequisites:** Android Studio (or a valid JDK 17 / Gradle environment)

1. Clone the repository.
2. Open the project in Android Studio (it will automatically download Gradle and the Android SDK).
3. Connect your Android device or spin up an Emulator.
4. Click **Run**.

## 2. Build via GitHub Actions (Cloud)
If you don't have Android Studio installed, you can compile the APK entirely in the cloud!
Whenever you `git push` to the `main` branch, our GitHub Action `.github/workflows/build-apk.yml` spins up a secure server, compiles the code, and attaches a downloadable `app-debug.apk` directly in the Actions tab.

## 3. Core Features
- **Offline First**: All data is stored locally in an embedded SQLite database (Room).
- **Auto-Backup**: Every change automatically overwrites a `.csv` file in your device's `Documents/Treadmill Logs` folder for total data portability.
- **Maintenance Tracking**: Warns you precisely when you need to oil your treadmill belt according to your rolling 7-day usage intervals.
