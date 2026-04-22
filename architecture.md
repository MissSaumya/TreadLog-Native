# TreadLog (Formerly Treadmill Tracker) - Architecture and Technical Documentation

## 1. System Overview
TreadLog is a mobile-first, hybrid PWA designed to track daily treadmill usage and maintenance schedules. The application is built as a Client-Side Single Page Application (SPA), utilizing Vite, React, and Capacitor. It can be run on the web or compiled directly into an Android APK, ensuring it runs completely offline.

## 2. Technology Stack
*   **Core Framework**: React 19, TypeScript, Vite
*   **Styling**: Tailwind CSS v4
*   **Icons**: `lucide-react`
*   **Date Operations**: `date-fns`
*   **Interaction/Navigation**: `react-swipeable`
*   **Mobile Wrapper / Native APIs**: Capacitor 8 (`@capacitor/core`, `@capacitor/filesystem`, `@capacitor/share`)
*   **Native Build Automation**: GitHub Actions (`build-apk.yml`)
*   **Build Assets**: `sharp` and `@capacitor/assets` (for automated logo scaling)

## 3. Data Architecture (Offline-First)
The application currently uses a fully local, offline-first data approach. No external database or backend server is required.

*   **Primary Web Datastore**: Browser `localStorage`.
*   **Primary Native Datastore**: Device internal filesystem (WhatsApp-style background sync via `@capacitor/filesystem`).
*   **Data Models**:
    *   `Entry`: Represents a single workout.
        *   `id` (string): UUID.
        *   `date` (string): Format 'yyyy-MM-dd'.
        *   `minutes` (number): Duration of the workout.
        *   `notes` (string): Optional text.
        *   `createdAt` (number): Epoch timestamp of when the log was created.
    *   **Maintenance State**:
        *   `treadmill_last_oiled_date` (string): ISO string of the last maintenance date.
        *   `treadmill_next_oiling_date` (string): ISO string of the projected next maintenance date.

## 4. Key Components and Modules
*   **`App.tsx`**: The main controller. Handles State Initialization, manages the bottom navigation router (Dashboard, Add, History, Maintenance), and implements globally scoped `useSwipeable` handlers for tab transitions. Note: `selectedDate` is lifted to this level to persist correctly between sweeps.
*   **`HistoryTab.tsx`**: Renders the historical list grouped seamlessly by Date and sorted descendingly. Contains the Web and Native Export/Import triggers.
*   **`useTreadmillData.ts`**: The core data engine. Controls reading/writing to `localStorage`, background synchronization to native storage via `nativeStorage.ts`, calculates weekly usage tiers, and computes oiling intervals.

## 5. Data Portability & Backup Strategy
*   **Web Fallback Flow**: Generates a BOM-encoded CSV string downloaded via Blob URIs. File name dynamically tags the date.
*   **Native Flow (Capacitor Android)**: 
    *   *Auto-Backup*: Employs a 'WhatsApp-style' auto-save mechanism using `@capacitor/filesystem`. Every time data is modified, it seamlessly overwrites a `TreadLog_History.csv` file directly inside the device's public Documents folder (`Documents/Treadmill Logs`).
    *   *System Recovery*: If `localStorage` is ever wiped but the app remains installed, it automatically searches the Documents folder for this CSV and restores the state during hydration.
*   **CSV Parsing**: Custom implemented in `csvSync.ts` to seamlessly handle nested commas within quotes and ignore BOM artifacts.

## 6. Project Configuration & App Icons
*   **Vite**: Configured in `vite.config.ts`.
*   **Capacitor**: Configured in `capacitor.config.ts` (`appName: TreadLog`).
*   **Build Automation**: 
    1. During `.github/workflows/build-apk.yml`, a pre-build step executes `generate-icon.js` using the `sharp` library.
    2. This script takes an SVG, adds a dark blue background, scales it, and writes `assets/logo.png`.
    3. Then, `@capacitor/assets generate --android` automatically slices the logo into 50+ micro-resolutions (adaptive icons, squircle, round, LDPI/MDPI) required by Android before the APK is finalized via Gradle.
