package com.example.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.PlatformInstagram
import com.example.ui.theme.PlatformTikTok
import com.example.ui.theme.PlatformYouTube

enum class ReelPlatform(
    val displayName: String,
    val shortName: String,
    val brandColor: Color,
    val packageNames: List<String>
) {
    INSTAGRAM(
        displayName = "Instagram Reels",
        shortName = "Instagram",
        brandColor = PlatformInstagram,
        packageNames = listOf("com.instagram.android")
    ),
    YOUTUBE(
        displayName = "YouTube Shorts",
        shortName = "Shorts",
        brandColor = PlatformYouTube,
        packageNames = listOf("com.google.android.youtube")
    ),
    TIKTOK(
        displayName = "TikTok",
        shortName = "TikTok",
        brandColor = PlatformTikTok,
        packageNames = listOf(
            "com.zhiliaoapp.musically",
            "com.ss.android.ugc.trill",
            "com.zhiliao.musically"
        )
    );

    companion object {
        fun fromPackageName(packageName: String?): ReelPlatform? {
            if (packageName == null) return null
            return entries.firstOrNull { platform ->
                platform.packageNames.any { pkg -> packageName.startsWith(pkg) }
            }
        }
    }
}
