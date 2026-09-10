package com.example.ui.legal

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.vector.ImageVector

enum class LegalTopic(
    val title: String,
    val shortSummary: String,
    val icon: ImageVector
) {
    PRIVACY_POLICY(
        title = "Privacy Policy",
        shortSummary = "100% offline, zero data collection, zero telemetry.",
        icon = Icons.Default.Lock
    ),
    TERMS_OF_SERVICE(
        title = "Terms of Service",
        shortSummary = "Personal digital wellness utility provided as-is.",
        icon = Icons.Default.Build
    ),
    ACCESSIBILITY_DATA_USE(
        title = "Accessibility & Data Use",
        shortSummary = "How Android Accessibility is used strictly to count reels.",
        icon = Icons.Default.Info
    ),
    DATA_DELETION(
        title = "Data Deletion",
        shortSummary = "Total user control over local database records and reset.",
        icon = Icons.Default.Delete
    ),
    THIRD_PARTY_DISCLAIMER(
        title = "Third-Party Disclaimer",
        shortSummary = "Independent utility not affiliated with Instagram, YouTube, or TikTok.",
        icon = Icons.Default.Warning
    ),
    CONTACT_SUPPORT(
        title = "Contact & Support",
        shortSummary = "Developer contact info, feedback channels, and support email.",
        icon = Icons.Default.Email
    )
}

object LegalContent {
    const val SUPPORT_EMAIL = "afnan.442252@gmail.com"
    const val APP_VERSION = "1.0.0"

    fun getContentForTopic(topic: LegalTopic): List<Pair<String, String>> {
        return when (topic) {
            LegalTopic.PRIVACY_POLICY -> listOf(
                "Zero Data Collection" to
                        "Reel Tracker is built with an absolute privacy-first philosophy. The application does not collect, record, transmit, or monetize any personal information, device identifiers, or usage telemetry. No external analytics SDKs or advertising trackers are included in this app.",
                "100% On-Device Processing" to
                        "All reel counts, timestamps, daily roll-over tallies, and weekly breakdown metrics are processed and stored solely on your local device within an encrypted private Room/SQLite database.",
                "No Internet Connection Required" to
                        "Reel Tracker functions entirely offline. It does not communicate with external servers, cloud databases, or AI APIs. Your habit data never leaves your physical phone."
            )
            LegalTopic.TERMS_OF_SERVICE -> listOf(
                "Personal Digital Wellness Utility" to
                        "Reel Tracker is designed strictly as a self-monitoring, mindfulness tool to help users track and moderate their short-form video consumption across popular media apps.",
                "Provided 'As-Is'" to
                        "The application is provided free of charge on an 'as-is' and 'as-available' basis without warranties of any kind. The developers are not liable for any discrepancies between third-party app usage statistics and Reel Tracker tallies.",
                "User Responsibility" to
                        "You retain full control over your device settings and permissions. You may enable, disable, or remove Reel Tracker at any time without restriction."
            )
            LegalTopic.ACCESSIBILITY_DATA_USE -> listOf(
                "Purpose of Accessibility Permission" to
                        "Android's AccessibilityService API is utilized for the sole purpose of detecting when short-form video feed activities (Instagram Reels, YouTube Shorts, and TikTok) are displayed on your screen and when swipe scroll gestures occur.",
                "Strict 8-Second Anti-Cheat Verification" to
                        "When a reel begins, Reel Tracker starts a local 8-second internal timer. If you swipe away before 8 continuous seconds, the timer immediately terminates with 0 count recorded. Only reels watched for at least 8 continuous seconds register as +1.",
                "Zero Content Or Keystroke Logging" to
                        "Reel Tracker does NOT read, capture, inspect, or log any video content, captions, audio, messages, passwords, credit card numbers, or user-identifying UI elements. Only window package names and scroll event types are processed."
            )
            LegalTopic.DATA_DELETION -> listOf(
                "Immediate Local Deletion" to
                        "You have 100% ownership of your data. You can erase all tracked history and reset lifetime statistics to 0 at any time via Settings > 'Clear All History & Reset to 0'.",
                "App Uninstall Cleanup" to
                        "Because all data resides exclusively in your device's internal application sandbox, uninstalling the application or selecting 'Clear Data' in Android System Settings completely and permanently purges all records immediately.",
                "No Residual Cloud Copies" to
                        "Since Reel Tracker operates with zero remote backends, there are no remote servers, backups, or cloud caches holding your records."
            )
            LegalTopic.THIRD_PARTY_DISCLAIMER -> listOf(
                "Independent Development" to
                        "Reel Tracker is an independent application and is not affiliated, associated, authorized, endorsed by, or in any way officially connected with Meta Platforms, Inc. (Instagram), Google LLC / Alphabet Inc. (YouTube), or ByteDance Ltd. (TikTok).",
                "Trademarks" to
                        "All product names, logos, brands, and registered trademarks mentioned are the property of their respective owners. Their mention in Reel Tracker is strictly for identification and compatibility purposes."
            )
            LegalTopic.CONTACT_SUPPORT -> listOf(
                "Developer & Support Email" to
                        "For questions, bug reports, feature requests, or privacy inquiries, please contact our support team at:\n\n$SUPPORT_EMAIL",
                "App Version & Build" to
                        "Reel Tracker Version: $APP_VERSION\nBuild: Production Local Release\nOperating System: Android (Jetpack Compose)",
                "Open Source & Transparency" to
                        "Reel Tracker is committed to total transparency. If you have suggestions to improve the 8-second detection engine or battery efficiency, feel free to reach out directly via email."
            )
        }
    }
}
