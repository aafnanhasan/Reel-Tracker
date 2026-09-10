package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.ReelPlatform
import com.example.ui.theme.BorderSubtle
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
fun PlatformBreakdownSection(
    instagramCount: Int,
    youtubeCount: Int,
    tiktokCount: Int,
    totalToday: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceCard)
            .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
            .testTag("platform_breakdown_section")
    ) {
        // Section header inside card
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Platform Breakdown",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary
            )
            Text(
                text = if (totalToday > 0) "$totalToday today" else "0 today",
                style = MaterialTheme.typography.labelSmall,
                color = TextTertiary
            )
        }

        HorizontalDivider(color = BorderSubtle, thickness = 1.dp)

        // Instagram Row
        PlatformRow(
            platformName = "Instagram Reels",
            iconResId = R.drawable.ic_platform_instagram,
            iconTint = Color.White,
            iconBgColor = PlatformInstagram,
            count = instagramCount,
            totalToday = totalToday,
            testTagPrefix = "instagram"
        )

        HorizontalDivider(color = BorderSubtle, thickness = 1.dp)

        // YouTube Shorts Row
        PlatformRow(
            platformName = "YouTube Shorts",
            iconResId = R.drawable.ic_platform_youtube,
            iconTint = Color.White,
            iconBgColor = PlatformYouTube,
            count = youtubeCount,
            totalToday = totalToday,
            testTagPrefix = "youtube"
        )

        HorizontalDivider(color = BorderSubtle, thickness = 1.dp)

        // TikTok Row
        PlatformRow(
            platformName = "TikTok",
            iconResId = R.drawable.ic_platform_tiktok,
            iconTint = Color.White,
            iconBgColor = PlatformTikTok,
            count = tiktokCount,
            totalToday = totalToday,
            testTagPrefix = "tiktok"
        )
    }
}

@Composable
private fun PlatformRow(
    platformName: String,
    iconResId: Int,
    iconTint: Color,
    iconBgColor: Color,
    count: Int,
    totalToday: Int,
    testTagPrefix: String
) {
    val percentage = if (totalToday > 0) (count * 100) / totalToday else 0

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .testTag("platform_row_$testTagPrefix"),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left: Platform Icon and Info
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = iconResId),
                    contentDescription = platformName,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = platformName,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                Text(
                    text = if (totalToday > 0 && count > 0) "$count reels • $percentage%" else "$count reels watched",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
        }

        // Right: Count Display (Pure auto-tracking, no manual buttons)
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.padding(start = 12.dp)
        ) {
            Text(
                text = "$count",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            if (totalToday > 0 && count > 0) {
                Text(
                    text = "$percentage%",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextTertiary
                )
            }
        }
    }
}
