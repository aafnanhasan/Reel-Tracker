package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ChevronRight
import com.example.ui.legal.LegalContent
import com.example.ui.legal.LegalTopic
import com.example.ui.legal.SingleLegalTopicDialog
import com.example.ui.legal.FullTermsAndConditionsDialog
import com.example.R
import com.example.service.ReelAccessibilityService
import com.example.ui.ReelTrackerViewModel
import com.example.ui.theme.AccentAmber
import com.example.ui.theme.AccentBlue
import com.example.ui.theme.AccentEmerald
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CanvasBg
import com.example.ui.theme.PlatformInstagram
import com.example.ui.theme.PlatformTikTok
import com.example.ui.theme.PlatformYouTube
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardAlt
import com.example.ui.theme.SurfaceSubtle
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary

@Composable
fun SettingsScreen(
    viewModel: ReelTrackerViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isAccessibilityRunning by remember { mutableStateOf(false) }
    var showClearConfirmDialog by remember { mutableStateOf(false) }
    var activeLegalTopic by remember { mutableStateOf<LegalTopic?>(null) }
    var showFullTermsDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.refreshDate()
        isAccessibilityRunning = ReelAccessibilityService.isAccessibilityEnabled(context)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CanvasBg)
            .padding(horizontal = 20.dp)
            .testTag("settings_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))
            Column {
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Tracking preferences, 8-second rule, and device setup",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextTertiary
                )
            }
        }

        // Service Status Hero Card
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceCard)
                    .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Auto-Detection Service",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(if (isAccessibilityRunning) AccentEmerald else AccentAmber)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isAccessibilityRunning) "Tracking Active" else "Permission Required",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isAccessibilityRunning) AccentEmerald else AccentAmber,
                                fontSize = 13.sp
                            )
                        }
                    }

                    Switch(
                        checked = isAccessibilityRunning,
                        onCheckedChange = {
                            ReelAccessibilityService.openAccessibilitySettings(context)
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = AccentEmerald,
                            checkedTrackColor = AccentEmerald.copy(alpha = 0.3f),
                            uncheckedThumbColor = TextTertiary,
                            uncheckedTrackColor = SurfaceSubtle
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = BorderSubtle, thickness = 1.dp)
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (isAccessibilityRunning) {
                        "Background tracking is running smoothly. When you watch reels or shorts for at least 8 seconds, each one is automatically counted."
                    } else {
                        "Tap below to enable Reel Tracker in Android Accessibility. Once turned on, reels will be counted automatically."
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isAccessibilityRunning) SurfaceCardAlt else AccentEmerald.copy(alpha = 0.15f))
                        .border(
                            1.dp,
                            if (isAccessibilityRunning) BorderSubtle else AccentEmerald.copy(alpha = 0.4f),
                            RoundedCornerShape(8.dp)
                        )
                        .clickable { ReelAccessibilityService.openAccessibilitySettings(context) }
                        .padding(vertical = 12.dp)
                        .testTag("open_accessibility_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = null,
                            tint = if (isAccessibilityRunning) TextPrimary else AccentEmerald,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isAccessibilityRunning) "Open Accessibility Settings" else "Turn ON Reel Tracker in Settings",
                            style = MaterialTheme.typography.labelLarge,
                            color = if (isAccessibilityRunning) TextPrimary else AccentEmerald,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // Strict 8-Second Anti-Cheat Card
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceCard)
                    .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "8-Second Rule",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(AccentEmerald.copy(alpha = 0.12f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "No Cheating Enforced",
                            style = MaterialTheme.typography.labelSmall,
                            color = AccentEmerald,
                            fontWeight = FontWeight.Medium,
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "To guarantee honest counting, a reel or short only registers after being watched continuously for at least 8 seconds. If you swipe to the next reel before 8 seconds are reached, the timer immediately cancels and zero count is recorded.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    RuleFeaturePill(label = "⏱ 8s Minimum Watch Time", modifier = Modifier.weight(1f))
                    RuleFeaturePill(label = "🚫 Swiping Early Discards", modifier = Modifier.weight(1f))
                }
            }
        }

        // Monitored Apps
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceCard)
                    .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
                    .padding(18.dp)
            ) {
                Text(
                    text = "Supported Video Platforms",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Automatically recognized when active on screen:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                MonitoredPlatformRow(
                    name = "Instagram Reels",
                    pkg = "com.instagram.android",
                    iconResId = R.drawable.ic_platform_instagram,
                    color = PlatformInstagram
                )
                Spacer(modifier = Modifier.height(12.dp))
                MonitoredPlatformRow(
                    name = "YouTube Shorts",
                    pkg = "com.google.android.youtube",
                    iconResId = R.drawable.ic_platform_youtube,
                    color = PlatformYouTube
                )
                Spacer(modifier = Modifier.height(12.dp))
                MonitoredPlatformRow(
                    name = "TikTok",
                    pkg = "com.zhiliaoapp.musically",
                    iconResId = R.drawable.ic_platform_tiktok,
                    color = PlatformTikTok
                )
            }
        }

        // Simple Setup Help (Android 13+ Restricted Settings)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceCard)
                    .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
                    .padding(18.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = AccentBlue,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Android Permission Help",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "If Android 13 or newer shows 'Restricted setting' when turning on Accessibility:\n" +
                            "1. Tap 'Open App Info' below.\n" +
                            "2. Tap the ⋮ (three dots) in the top-right corner.\n" +
                            "3. Select 'Allow restricted settings' and confirm your PIN.\n" +
                            "4. Return here and enable Reel Tracker.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceCardAlt)
                        .border(1.dp, BorderSubtle, RoundedCornerShape(8.dp))
                        .clickable {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                data = Uri.parse("package:${context.packageName}")
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            }
                            context.startActivity(intent)
                        }
                        .padding(vertical = 12.dp)
                        .testTag("open_app_info_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Open App Info",
                        style = MaterialTheme.typography.labelLarge,
                        color = TextPrimary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Local Storage & Reset Card
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceCard)
                    .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
                    .padding(18.dp)
            ) {
                Text(
                    text = "Data & Privacy",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "All counts are stored 100% locally in your device's private SQLite database. No accounts, no clouds, and no internet transmission.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceCardAlt)
                        .border(1.dp, BorderSubtle, RoundedCornerShape(8.dp))
                        .clickable { showClearConfirmDialog = true }
                        .padding(vertical = 12.dp)
                        .testTag("clear_all_data_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Clear All History & Reset to 0",
                        style = MaterialTheme.typography.labelLarge,
                        color = PlatformYouTube,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Policies, Legal & Support Section
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceCard)
                    .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
                    .padding(18.dp)
                    .testTag("policies_and_support_section")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Legal, Policies & Support",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Transparency, privacy policy, and developer contact",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextTertiary,
                            fontSize = 12.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(AccentBlue.copy(alpha = 0.12f))
                            .clickable { showFullTermsDialog = true }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .testTag("view_all_terms_button")
                    ) {
                        Text(
                            text = "View All",
                            style = MaterialTheme.typography.labelSmall,
                            color = AccentBlue,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                LegalTopic.entries.forEachIndexed { index, topic ->
                    if (index > 0) {
                        HorizontalDivider(
                            color = BorderSubtle.copy(alpha = 0.5f),
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                    }
                    LegalTopicSettingRow(
                        topic = topic,
                        onClick = { activeLegalTopic = topic }
                    )
                }
            }
        }

        // App Footer Info
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Reel Tracker v${LegalContent.APP_VERSION}",
                    style = MaterialTheme.typography.labelMedium,
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "100% Offline • No Cloud • Zero Data Collection",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextTertiary,
                    fontSize = 11.sp
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(28.dp))
        }
    }

    // Clear All Confirmation Dialog
    if (showClearConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showClearConfirmDialog = false },
            containerColor = SurfaceCard,
            title = {
                Text(
                    text = "Clear all reel records?",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary
                )
            },
            text = {
                Text(
                    text = "This will erase all past records and reset your lifetime counter to 0. This cannot be undone.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.clearAllData()
                        showClearConfirmDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PlatformYouTube),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text("Delete Everything", color = TextPrimary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearConfirmDialog = false }) {
                    Text("Cancel", color = TextTertiary)
                }
            }
        )
    }

    // Single Topic Dialog
    activeLegalTopic?.let { topic ->
        SingleLegalTopicDialog(
            topic = topic,
            onDismissRequest = { activeLegalTopic = null }
        )
    }

    // Full Terms & Conditions Modal
    if (showFullTermsDialog) {
        FullTermsAndConditionsDialog(
            onDismissRequest = { showFullTermsDialog = false },
            onAccept = { showFullTermsDialog = false }
        )
    }
}

@Composable
private fun LegalTopicSettingRow(
    topic: LegalTopic,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 4.dp)
            .testTag("setting_legal_row_${topic.name.lowercase()}"),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(SurfaceCardAlt),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = topic.icon,
                    contentDescription = null,
                    tint = AccentBlue,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = topic.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary
                )
                Text(
                    text = topic.shortSummary,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextTertiary,
                    fontSize = 11.sp
                )
            }
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = TextTertiary,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
private fun RuleFeaturePill(
    label: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(SurfaceCardAlt)
            .border(1.dp, BorderSubtle, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary,
            fontSize = 11.sp
        )
    }
}

@Composable
private fun MonitoredPlatformRow(
    name: String,
    pkg: String,
    iconResId: Int,
    color: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(color),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = iconResId),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary
                )
                Text(
                    text = pkg,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextTertiary,
                    fontSize = 11.sp
                )
            }
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(AccentEmerald.copy(alpha = 0.1f))
                .padding(horizontal = 6.dp, vertical = 3.dp)
        ) {
            Text(
                text = "Monitored",
                style = MaterialTheme.typography.labelSmall,
                color = AccentEmerald,
                fontSize = 10.sp
            )
        }
    }
}
