# TreadLog - Architecture and Technical Documentation

## 1. System Overview
TreadLog is a mobile-first, native Android application designed to track daily treadmill usage and maintenance schedules. The application is written entirely in Kotlin using Jetpack Compose for a buttery-smooth, natively gestured UI. It runs completely offline with no backend components.

## 2. Technology Stack
*   **Language**: Kotlin
*   **UI Framework**: Jetpack Compose (Material Design 3)
*   **Architecture Pattern**: MVVM (Model-View-ViewModel) utilizing Unidirectional Data Flow via `StateFlow`.
*   **Navigation**: Jetpack Navigation Compose coupled with `HorizontalPager` for seamless swipe transitions.
*   **Build Automation**: GitHub Actions (`build-apk.yml`) leveraging Gradle.

## 3. Data Architecture (Offline-First)
The application ensures the user completely owns their data on-device.
*   **Primary Datastore**: **Room Database** (SQLite) handles lightning-fast insertions and querying.
*   **Secondary State Store**: **Datastore Preferences** manages simple scalar states like the `last_oiled_date`.
*   **Backup Datastore**: Native File I/O writing to the Android public `Documents/Treadmill Logs` directory.
*   **Data Models**:
    *   `WorkoutEntry`: Room Entity defining the SQLite schema (`id`, `date`, `minutes`, `notes`, `createdAt`).
    *   `AppState`: The UI State holder consumed by Jetpack Compose.

## 4. Key Components and Modules
*   **`MainActivity.kt`**: The root Compose activity. Initializes the App Theme, binds the `MainViewModel`, and contains the `Scaffold`/`HorizontalPager` that controls the four bottom tabs.
*   **`com.treadlog.ui.screens`**: Contains the decoupled Compose views (`DashboardScreen`, `AddWorkoutScreen`, `HistoryScreen`, `MaintenanceScreen`).
*   **`MainViewModel.kt`**: Bridges the Room DAO and the UI. Recomputes rolling-averages and pushes UI state updates.
*   **`DomainLogic.kt`**: Contains the pure Kotlin math functions governing the rolling-week logic and the CSV generation.
*   **`CsvHelper.kt`**: Translates AppState into the fallback `TreadLog_History.csv` and streams it directly to the system's external storage without requiring the user to tap "Export".

## 5. Build Pipeline
*   **Gradle**: Configured via `.kts` (Kotlin Script) files.
*   **GitHub Actions**: Triggers on `push` to main. provisions Ubuntu, sets up JDK 17, and runs `gradle assembleDebug` to churn out an artifact APK.
