# TreadLog - App Recreation Specifications

*If this app ever needs to be rebuilt from scratch, feeding this document to an AI or developer will ensure all previously solved headaches, features, and specific design choices are perfectly replicated.*

## 1. Core Identity
*   **App Name**: `TreadLog` (Formally Treadmill Tracker).
*   **Logo/Icon**: A sleek navy blue background (`#0f172a`) with a bright orange stylized runner figure inside a light blue glow ring. Generated dynamically using `sharp` at build time.
*   **Primary Purpose**: An offline-first mobile app compiled via Capacitor to track treadmill usage minutes and monitor treadmill belt lubrication schedules.

## 2. Features & UI Layout
The app uses a bottom-tab navigation structure mapped to URL hashes (to support Android physical back-buttons) and `react-swipeable` for native horizontal gestures across four sections:

1.  **Dashboard**: Summary statistics (total minutes, quick view of upcoming maintenance).
2.  **Add**: Input a new workout. Fields: Date Picker, Duration, Notes. 
    *   *Crucial Details*: Features a highly visible "30 Min Preset" button. Contextually saves the selected date state even if the user swipes away to look at the History tab and swipes back.
3.  **History**: grouped chronologically by actual logged dates in a visually tight UI (not floating padded boxes). Users can Edit/Delete inline.
4.  **Maintenance**: 3 sections, ordered Status (Top), Input Form (Middle), Maintenance Rules Math (Bottom).

## 2.5 Business Logic & Algorithms
Future implementations MUST respect the exact math and logic previously defined:

**A. Unique Weekly Logic (Not Monday-Sunday)**
*   The application does *not* use a rigid Calendar week (e.g., Monday through Sunday).
*   **The "First Run" Rule**: A week is defined as a rolling 7-day period starting exactly from the **first date the user ever recorded a run**.
*   *Implementation*: The algorithm grabs the earliest `date` from all `entries`, sets that as Day 0, and chunks all subsequent historical data into 7-day blocks relative to that anchor date.

**B. The Oiling Engine & Thresholds**
*   Treadmill maintenance is determined dynamically by the user's "Peak Historical Usage Week". The app grabs `maxMinutes` across all rolling weeks.
    *   **Low Usage** (< 3 hours / 180 mins/w): Require oiling every **60 Days**.
    *   **Medium Usage** (3-7 hours / 180-479 mins/w): Require oiling every **30 Days**.
    *   **High Usage** (> 8 hours / 480+ mins/w): Require oiling every **15 Days**.
*   **The Math Rule**: We use `addDays(lastDate, interval - 1)` because the actual day they oil the machine physically counts as 'Day 1'. 
*   **Alert Threshold**: "Is Due Soon" triggers when the calculated Next Oiling Date is <= **3 days** away.

## 3. Data Storage (Crucial constraint)
*   **Web Sandbox**: Browser LocalStorage.
*   **Native Device Context**: Capacitor Filesystem auto-saves to `Documents/Treadmill Logs`. It operates precisely like WhatsApp local backups.

## 4. CSV Import & Export Rules
Since data is natively backed up, the manual/fallback CSV flow must be exact:
*   **Export Attributes**:
    *   Filename must be standardized: `TreadLog-{date}.csv`.
    *   The CSV string MUST start with a UTF-8 BOM (`\uFEFF`) to prevent Excel from corrupting the document.
    *   Format headers: `Date, Minutes, Notes, Logged At, Last Oiled Date, Next Oiling Date`.
*   **Import Attributes**:
    *   Must use a custom, robust CSV parser. Excel automatically wraps notes/text containing commas in quotes. The parser uses a RegExp split logic to ignore commas buried inside quotes.
    *   Importing MUST deduplicate by checking existing combinations of date/minutes. It must also elegantly hydrate missing notes/oiling dates.

## 5. Development Rules
*   **Styling**: Use bare Tailwind CSS for a clean, minimalist UI (slate, blue, white, sepia).
*   **Build Pipeline**: Runs completely out of GitHub Actions. Requires Node, Java 21, `sharp` for svg-to-png logo scaling, `@capacitor/assets` for Android shredding, and Gradle to output the final debug APK.
