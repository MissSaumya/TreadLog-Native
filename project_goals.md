# TreadLog - Project Goals & Roadmap

## 1. Project Purpose
The goal of TreadLog is to provide a lightning-fast, privacy-first, offline-capable mobile application tailored specifically for treadmill owners. Unlike bloated fitness apps that track GPS, heart rate, and social feeds, this app focuses on doing two things perfectly:
1. Logging daily treadmill usage duration.
2. Tracking belt lubrication/maintenance schedules based on physical usage and elapsed time.

## 2. Core Functional Requirements
Any developer modifying this Kotlin app must ensure these requirements are met and preserved:

*   **Native Gestures**: Swiping to navigate between Bottom Navigation views via `HorizontalPager` must remain smooth. No web-view jitter or tap-delays.
*   **Data Portability & Auto-sync**: The user must *always* own their data. A full CSV auto-backup seamlessly dropping into the device folder (`Documents/Treadmill Logs`) is critical. 
*   **Maintenance Forecasting**: The app must clearly alert the user when their treadmill needs to be oiled, extending the life of their equipment. The algorithm must remain 15/30/60 offsets and count the *day oiled* as Day 1.

## 3. Current Feature Set
*   **Dashboard**: Displays at-a-glance metrics (total minutes ran across all history, quick view of upcoming maintenance).
*   **Add Entry**: A simple form taking Duration (minutes), Date, and optional Notes. Contextually saves the selected date. Includes a convenient "30 Min Preset".
*   **History & Management**: A complete log of all workouts seamlessly grouped by actual logged dates queried directly from the SQLite database.
*   **Maintenance Tab**: Visually lays out the timeline logic and dynamically tells the user exactly what usage tier they fall into.

## 4. Known Edge Cases and Developer Context
*   **Android File Permissions**: In modern Android (10+), scoped storage makes writing to the device system difficult. By using `Environment.getExternalStoragePublicDirectory`, and allowing our own app to generate the CSV from scratch, we avoid the intense overhead of forcing the user through Storage Access Framework document trees.
*   **Rolling Week Math**: A "Week" is not Monday to Sunday. Day 0 of the first week is determined by sorting the database and finding the very first chronological run the user ever logged.

## 5. Future Roadmap
If the usage of this app scales across multiple distinct devices for the same user, local isolation via Room/SQLite must be expanded.
**Proposed Architecture for Multi-Device Sync:**
*   Implement Firebase Auth.
*   Replace Room DAO with Firestore Realtime listeners while utilizing Firestore's built-in offline caches.
