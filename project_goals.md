# TreadLog - Project Goals & Roadmap

## 1. Project Purpose
The goal of TreadLog is to provide a lightning-fast, privacy-first, offline-capable mobile application tailored specifically for treadmill owners. Unlike bloated fitness apps that track GPS, heart rate, and social feeds, this app focuses on doing two things perfectly:
1. Logging daily treadmill usage duration.
2. Tracking belt lubrication/maintenance schedules based on physical usage and elapsed time.

## 2. Core Functional Requirements
Any developer taking over or contributing to this project must ensure the following requirements are met and preserved:

*   **Offline Functionality**: The app must fully load and allow data entry natively without an internet connection.
*   **Frictionless Entry**: Logging a workout should take fewer than 3 taps. Swiping to navigate between views must remain smooth.
*   **Data Portability & Auto-sync**: The user must *always* own their data. Full CSV auto-backup to native folders (`Documents/Treadmill Logs`) is critical. 
*   **Maintenance Forecasting**: The app must clearly alert the user when their treadmill needs to be oiled, extending the life of their equipment. The algorithm must remain 14/30/60 offsets and count the *day oiled* as Day 1.
*   **Mobile-Native Feel**: Tap targets must be large (>44px), scrolling should be smooth, swipe navigation must work natively, and the back-button hash-routing should operate exactly like a compiled application.

## 3. Current Feature Set
*   **Dashboard**: Displays at-a-glance metrics (total minutes walked/ran, quick view of upcoming maintenance).
*   **Add Entry**: A simple form taking Duration (minutes), Date, and optional Notes. Contextually saves the selected date even if navigating away and returning.
*   **History & Management**: A complete log of all workouts seamlessly grouped by actual logged dates. Users can effortlessly edit, delete, or import backups.
*   **Maintenance Tab**: Visually lays out the timeline logic (Top-to-Bottom: Status -> Input Form -> Math Logic rules). 

## 4. Known Edge Cases and Developer Context
*   **Capacitor Storage Sync limitations**: We bypass native Android `Storage Access Framework` permission complexities by relying on the public standard `Directory.Documents`. When debugging the Android side, ensure `Treadmill Logs` folder write permissions are properly evaluated.
*   **CSV Parsing**: Excel defaults to saving CSVs with a UTF-8 BOM (`\uFEFF`) and places quotes around fields containing commas. The custom CSV parser handles these edge cases. Do not replace it with a simplistic `split(',')`.
*   **Icon Generation**: Since we compile via GitHub Actions, native icons are explicitly created at build time using the Node `sharp` CLI on an SVG, converted into `assets/logo.png`, then shredded into adaptive icons via `@capacitor/assets`.

## 5. Potential Future Roadmap / Architect Considerations

If the user complains about auto-sync limitations across *entirely separate devices* (e.g. iPad and Android Phone), the only path forward is to discard local isolation and build an authenticated sync layer.

**Proposed Phase 2 Architecture (If specifically requested):**
*   Integrate backend cloud sync natively.
*   Implement Firebase Auth (Email/Password or Google Auth).
*   Switch CSV local backups strictly over to Firestore sub-collections.
