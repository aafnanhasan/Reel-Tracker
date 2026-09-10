# Reel Tracker 📱

A modern, privacy-first Android utility that automatically tracks your short-form video consumption across **Instagram Reels**, **YouTube Shorts**, and **TikTok**. Built to encourage digital mindfulness without manual counting, clunky timers, or cloud intrusion.

[![Android](https://img.shields.io/badge/Platform-Android_8.0+_(API_24+)-3DDC84?logo=android&logoColor=white)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.10-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Build & Release APK](https://github.com/actions/workflows/build-apk.yml/badge.svg)](../../actions)
[![Offline First](https://img.shields.io/badge/Privacy-100%25_Offline-blue)](#-privacy-permissions--accessibility)

---

##  Download & Install APK

You can download and install Reel Tracker directly on your Android phone through GitHub:

### Option 1: Download from GitHub Releases (Recommended)
1. Head over to the **[Releases](../../releases)** section on the right-side panel of this GitHub repository (or navigate to `Releases > Latest`).
2. Under **Assets**, tap **`ReelTracker.apk`** to download the latest installer directly to your device.
3. Once downloaded, tap the notification or open your **Files / Downloads** app and select `ReelTracker.apk`.

### Option 2: Download from GitHub Actions (Latest CI Build)
1. Click the **[Actions](../../actions)** tab at the top of this repository.
2. Select the latest successful run under **Build & Release APK**.
3. Scroll down to the **Artifacts** section at the bottom of the summary page and click **`ReelTracker-APK`** to download.

---

### 📲 Sideloading & Android Installation Guide

When installing Reel Tracker for the first time outside of Google Play, follow these quick steps:

1. **Allow Installation from Unknown Sources**:
   - When tapping the APK, Android may ask: *"For your security, your phone is not allowed to install unknown apps from this source"*.
   - Tap **Settings** on the prompt, then toggle **Allow from this source**.
   - Tap back and proceed with **Install**.

2. **Enable Tracking in Accessibility**:
   - Open **Reel Tracker** and tap **Turn ON Reel Tracker in Settings** (or go to **Phone Settings > Accessibility > Installed Apps > Reel Tracker**).
   - Toggle the switch to **On**.

3. **Android 13+ "Restricted Setting" Fix**:
   - If Android shows a popup saying *"Restricted setting: For your security, this setting is currently unavailable"*:
     1. Open your phone's **Settings > Apps > Reel Tracker**.
     2. Tap the **⋮** (three vertical dots) in the upper-right corner.
     3. Tap **Allow restricted settings** and authenticate using your device PIN or fingerprint.
     4. Return to **Accessibility** and turn on **Reel Tracker**.

---

## 🚀 Pushing this Project to GitHub (From Google AI Studio)

If you are using Google AI Studio Build and want to publish or update this repository:
1. Open the top-right header / settings menu in Google AI Studio.
2. Click **Export** or **Push to GitHub**.
3. Authenticate with your GitHub account and choose a new or existing repository.
4. Once pushed, the included **GitHub Actions workflow** (`.github/workflows/build-apk.yml`) will automatically trigger, compile the `.apk`, and prepare it for download under **Actions** and **Releases**!

---

## ✨ Key Features

- **Automated Background Tracking**: Uses Android's native Accessibility Service to detect when supported video feeds are active on-screen and when scrolls occur.
- **Strict 8-Second Anti-Cheat Rule**: A reel or short only increments your count if watched continuously for **at least 8 seconds**. Swiping away prematurely cancels the timer with **zero count recorded**.
- **100% Offline & Private**:
  - All data is saved exclusively inside a local SQLite (Room) database on your phone.
  - **Zero cloud servers, zero analytics SDKs, and zero telemetry**.
  - **No Gemini API key or external service keys required**. The app operates fully self-contained.
- **Midnight Rollover & Permanent Records**:
  - Automatically preserves daily totals at midnight (12:00 AM) and archives them in the Records tab.
  - Weekly distribution analytics and lifetime milestone counters.
- **Total User Data Control**:
  - Single-tap data reset in Settings to permanently purge all stored counts.
  - Built-in transparent legal disclosures, privacy policies, and support channels.

---

## 🛡️ Privacy, Permissions & Accessibility

Reel Tracker is engineered under a strict **Zero Data Collection** policy:

| Permission | Purpose | Data Collected |
| :--- | :--- | :--- |
| `BIND_ACCESSIBILITY_SERVICE` | Detects when Instagram, YouTube, or TikTok feed is on screen and senses scroll gestures. | **None.** No video content, text captions, keystrokes, audio, or account credentials are ever read or logged. |

### Android 13+ "Restricted Setting" Setup
On Android 13 and newer, sideloaded applications require permission confirmation to enable Accessibility:
1. Go to **Settings > Apps > Reel Tracker**.
2. Tap the **⋮** (three vertical dots) menu in the top-right corner.
3. Select **Allow restricted settings** and authenticate with your device PIN / fingerprint.
4. Return to Reel Tracker and enable the tracking service.

---

## 🏛️ Project Architecture

- **UI Framework**: Modern Jetpack Compose with Material Design 3 (M3).
- **Architecture Pattern**: MVVM (Model-View-ViewModel) with reactive Kotlin `StateFlow` and Coroutines.
- **Local Persistence**: Jetpack Room Database (`ReelDatabase`, `ReelRecord`, `ReelDao`).
- **Detection Engine**: `ReelAccessibilityService` observing `TYPE_VIEW_SCROLLED`, `TYPE_WINDOW_STATE_CHANGED`, and `TYPE_WINDOW_CONTENT_CHANGED` with debounced 8-second dwell coroutine jobs.

```
app/src/main/java/com/example/
├── data/
│   ├── ReelDao.kt             # Room database operations & daily queries
│   ├── ReelDatabase.kt        # Room database definition
│   ├── ReelRecord.kt          # Daily reel entity with platform breakdown
│   └── ReelRepository.kt      # Repository coordinating auto-increments & resets
├── model/
│   └── ReelPlatform.kt        # Enum for INSTAGRAM, YOUTUBE, and TIKTOK packages
├── service/
│   └── ReelAccessibilityService.kt # 8-second dwell detection engine
├── ui/
│   ├── components/            # Metric cards, platform breakdowns & visual indicators
│   ├── legal/                 # Privacy Policy, Terms, Accessibility disclosure & Dialogs
│   ├── screens/
│   │   ├── WelcomeScreen.kt   # First-launch onboarding with Continue & Terms popup
│   │   ├── TodayScreen.kt     # Real-time daily counter & platform breakdown
│   │   ├── ReelsRecordScreen.kt # Historical logs, charts & volume trends
│   │   └── SettingsScreen.kt  # Service controls, 8s rule info & Legal/Support
│   ├── theme/                 # Obsidian-dark palette, typography & elevation
│   ├── MainScreen.kt          # Bottom navigation bar & first-launch routing
│   └── ReelTrackerViewModel.kt # App state management
└── MainActivity.kt            # Edge-to-edge entry point
```

---

## 📄 Legal & Policies

Reel Tracker includes complete in-app policies accessible from the first-launch Welcome popup or at the bottom of the Settings screen:
- **Privacy Policy**: Details 100% on-device processing and lack of network calls.
- **Terms of Service**: Outlines personal digital wellness usage conditions.
- **Accessibility & Data Use**: Full transparency on event detection mechanics.
- **Data Deletion**: Complete user control over database erasure.
- **Third-Party Disclaimer**: Independent tool; not affiliated with Meta (Instagram), Alphabet (YouTube), or ByteDance (TikTok).
- **Contact / Support**: Direct developer contact channel (`afnan.442252@gmail.com`).

---

## 🛠️ Build & Development

### Prerequisites
- Android Studio Ladybug or newer
- JDK 17
- Android SDK 36 (Minimum SDK: 24)

### Building via Terminal
```bash
# Compile and assemble debug APK
gradle :app:assembleDebug

# Run unit and local JVM tests
gradle :app:testDebugUnitTest
```

---

## 📬 Support & Inquiries
For support, feedback, or inquiries, reach out to **afnan.442252@gmail.com**.
