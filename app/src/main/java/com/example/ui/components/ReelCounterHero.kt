package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LifetimeStats
import com.example.data.ReelRecord
import com.example.ui.theme.AccentBlue
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.PlatformInstagram
import com.example.ui.theme.PlatformTikTok
import com.example.ui.theme.PlatformYouTube
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardAlt
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import java.util.Locale

@Composable
fun ReelCounterHero(
    record: ReelRecord,
    lifetimeStats: LifetimeStats,
    modifier: Modifier = Modifier
) {
    val totalToday = record.totalCount

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceCard)
            .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
            .testTag("reel_counter_hero")
    ) {
        // Primary Today Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "TODAY'S REELS",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextTertiary,
                    letterSpacing = 1.2.sp
                )

                if (totalToday > 0) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(SurfaceCardAlt)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "Reset at 12:00 AM",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextTertiary,
                            fontSize = 10.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Dominant main counter
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "$totalToday",
                    style = MaterialTheme.typography.displayLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = if (totalToday == 1) "reel watched" else "reels watched",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSecondary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (totalToday > 0) {
                val igWeight = record.instagramCount.toFloat() / totalToday
                val ytWeight = record.youtubeCount.toFloat() / totalToday
                val ttWeight = record.tiktokCount.toFloat() / totalToday

                // Proportional Segment Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(SurfaceCardAlt)
                ) {
                    if (igWeight > 0) {
                        Box(
                            modifier = Modifier
                                .weight(igWeight)
                                .height(6.dp)
                                .background(PlatformInstagram)
                        )
                    }
                    if (ytWeight > 0) {
                        Box(
                            modifier = Modifier
                                .weight(ytWeight)
                                .height(6.dp)
                                .background(PlatformYouTube)
                        )
                    }
                    if (ttWeight > 0) {
                        Box(
                            modifier = Modifier
                                .weight(ttWeight)
                                .height(6.dp)
                                .background(PlatformTikTok)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Clean Minimalist Legend
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    LegendItem(name = "Instagram", count = record.instagramCount, color = PlatformInstagram)
                    LegendItem(name = "YouTube", count = record.youtubeCount, color = PlatformYouTube)
                    LegendItem(name = "TikTok", count = record.tiktokCount, color = PlatformTikTok)
                }
            } else {
                // Thoughtful, quiet empty state
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceCardAlt)
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = "No reels watched yet today. Swiping in Instagram, YouTube, or TikTok will be counted here automatically.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }
        }

        HorizontalDivider(color = BorderSubtle, thickness = 1.dp)

        // Integrated Secondary Statistics Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(SurfaceCardAlt.copy(alpha = 0.5f))
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Lifetime Metric
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "LIFETIME TOTAL",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextTertiary,
                    fontSize = 10.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${lifetimeStats.totalReels}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "all-time watched",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            }

            // Daily Average Metric
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "DAILY AVERAGE",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextTertiary,
                    fontSize = 10.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                val avgStr = if (lifetimeStats.dailyAverage > 0) {
                    String.format(Locale.US, "%.1f", lifetimeStats.dailyAverage)
                } else "0.0"
                Text(
                    text = avgStr,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "reels / active day",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            }

            // Active Days Metric
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "DAYS LOGGED",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextTertiary,
                    fontSize = 10.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${lifetimeStats.daysLogged}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "tracked days",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
private fun LegendItem(
    name: String,
    count: Int,
    color: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = "$name: ",
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary
        )
        Text(
            text = "$count",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
    }
}
