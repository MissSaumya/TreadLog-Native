# TreadLog - App Specifications and Rules

*This document serves as the absolute source of truth for the strict mathematical rules spanning the application. When modifying the Kotlin codebase, these behaviors must not be altered.*

## 1. Core Identity
*   **App Name**: `TreadLog`.
*   **Primary Purpose**: An offline-first Native Android application using Jetpack Compose to track treadmill usage minutes and monitor lubrication schedules.

## 2. Features & UI Layout
The app utilizes a standard Jetpack `Scaffold` coupled with a `NavigationBar` to host four horizontal pager screens:

1.  **Dashboard**: Summary statistics.
2.  **Add**: Input a new workout. Contains a quick "30 Min Preset" button.
3.  **History**: Queries the Room DB to fetch a descending list of historical logs. (Notes and durations).
4.  **Maintenance**: Displays the calculated cycle and next available date.

## 3. Business Logic & Algorithms (Crucial)
Future implementations MUST respect the exact math and logic previously defined in `DomainLogic.kt`:

**A. Unique Weekly Logic (Not Monday-Sunday)**
*   The application does *not* use a rigid Calendar week.
*   **The "First Run" Rule**: A week is defined as a rolling 7-day period starting exactly from the **first date the user ever recorded a run**.
*   *Implementation*: The algorithm grabs the earliest `date` from all SQLite entries, sets that as Day 0, and chunks all subsequent historical data into 7-day blocks utilizing Java `ChronoUnit.DAYS`.

**B. The Oiling Engine & Thresholds**
*   Treadmill maintenance is determined dynamically by the user's "Peak Historical Usage Week" looking at `maxMinutes`.
    *   **Low Usage** (< 3 hours / 180 mins/w): Require oiling every **60 Days**.
    *   **Medium Usage** (3-7 hours / 180-479 mins/w): Require oiling every **30 Days**.
    *   **High Usage** (> 8 hours / 480+ mins/w): Require oiling every **15 Days**.
*   **The Math Rule**: We use `.plusDays(interval - 1)` because the actual day they oil the machine physically counts as 'Day 1'. 

## 4. Data Storage & Backups
*   **Primary Sandbox**: SQLite via Android Room Database.
*   **Export/Backup Fallback**: A custom logic pipeline serializes the Room database into a comma-separated string, prepends a UTF-8 BOM (`\uFEFF`) to prevent Excel corruption, escapes internal commas using quoted wrappers, and flushes it continuously to `Documents/Treadmill Logs`.

## 5. Development Rules
*   **Styling**: Use Jetpack Compose Material 3 standard colors, enforcing a Dark Navy Theme (`#0f172a`) with Bright Orange actions.
*   **Build Pipeline**: Compiles exclusively through GitHub Actions (`setup-java`, `setup-gradle`, `gradle assembleDebug`), outputting a debug binary artefact.
