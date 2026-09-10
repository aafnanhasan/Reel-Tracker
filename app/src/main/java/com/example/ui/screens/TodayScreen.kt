package com.example.ui.screens

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.ReelPlatform
import com.example.service.ReelAccessibilityService
import com.example.ui.ReelTrackerViewModel
import com.example.ui.components.PlatformBreakdownSection
import com.example.ui.components.ReelCounterHero
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
fun TodayScreen(
    viewModel: ReelTrackerViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val todayRecord by viewModel.todayRecord.collectAsStateWithLifecycle()
    val lifetimeStats by viewModel.lifetimeStats.collectAsStateWithLifecycle()

    var isAccessibilityRunning by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.refreshDate()
        isAccessibilityRunning = ReelAccessibilityService.isAccessibilityEnabled(context)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CanvasBg)
            .padding(horizontal = 20.dp)
            .testTag("today_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))

            // Clean Header: App Title, Date, and Silent Status Indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Reel Tracker",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = todayRecord.date,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextTertiary
                    )
                }

                // Discreet, subtle status badge (no glowing aura or floating popup)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(SurfaceCardAlt)
                        .border(1.dp, BorderSubtle, RoundedCornerShape(6.dp))
                        .clickable { ReelAccessibilityService.openAccessibilitySettings(context) }
                        .padding(horizontal = 9.dp, vertical = 5.dp)
                        .testTag("status_indicator_badge")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(if (isAccessibilityRunning) AccentEmerald else AccentAmber)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isAccessibilityRunning) "Tracking Active" else "Tracking Inactive",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isAccessibilityRunning) AccentEmerald else AccentAmber,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // Hero Statistics Section
        item {
            ReelCounterHero(
                record = todayRecord,
                lifetimeStats = lifetimeStats
            )
        }

        // Unified Platform Breakdown Section
        item {
            PlatformBreakdownSection(
                instagramCount = todayRecord.instagramCount,
                youtubeCount = todayRecord.youtubeCount,
                tiktokCount = todayRecord.tiktokCount,
                totalToday = todayRecord.totalCount
            )
        }

        // Action Utilities: Reset Today's Count
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(SurfaceCardAlt)
                    .border(1.dp, BorderSubtle, RoundedCornerShape(8.dp))
                    .clickable { showResetDialog = true }
                    .padding(vertical = 12.dp)
                    .testTag("reset_today_button"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Reset Today's Count (0)",
                    style = MaterialTheme.typography.labelMedium,
                    color = TextTertiary
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Reset Confirmation Dialog
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            containerColor = SurfaceCard,
            title = {
                Text(
                    text = "Reset today's count?",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary
                )
            },
            text = {
                Text(
                    text = "This will set today's reels count back to 0. Your previous days' history and lifetime totals remain preserved.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.resetToday()
                        showResetDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SurfaceSubtle),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text("Reset to 0", color = TextPrimary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Cancel", color = TextTertiary)
                }
            }
        )
    }
}
