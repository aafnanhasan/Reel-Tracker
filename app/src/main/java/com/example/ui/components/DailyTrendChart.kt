package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.example.data.DailyTrendBar
import com.example.ui.theme.AccentBlue
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardAlt
import com.example.ui.theme.SurfaceSubtle
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary

@Composable
fun DailyTrendChart(
    bars: List<DailyTrendBar>,
    modifier: Modifier = Modifier
) {
    val maxCount = (bars.maxOfOrNull { it.count } ?: 0).coerceAtLeast(10)
    val totalCountInPeriod = bars.sumOf { it.count }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceCard)
            .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
            .padding(18.dp)
            .testTag("daily_trend_chart")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "7-Day Activity",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                Text(
                    text = "Daily reel volumes",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }

            Text(
                text = "$totalCountInPeriod reels past week",
                style = MaterialTheme.typography.labelSmall,
                color = TextTertiary
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Bars Container
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            bars.forEach { bar ->
                val barFraction = if (maxCount > 0) (bar.count.toFloat() / maxCount.toFloat()).coerceIn(0.06f, 1f) else 0.06f
                val barColor = if (bar.isToday) AccentBlue else if (bar.count > 0) SurfaceSubtle else SurfaceCardAlt

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    // Value above bar
                    Text(
                        text = if (bar.count > 0) "${bar.count}" else "-",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (bar.isToday) AccentBlue else TextTertiary,
                        fontSize = 10.sp,
                        fontWeight = if (bar.isToday) FontWeight.Bold else FontWeight.Normal
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Vertical Bar
                    Box(
                        modifier = Modifier
                            .width(20.dp)
                            .height(72.dp),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        // Background track
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(20.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(SurfaceCardAlt)
                        )

                        // Filled bar
                        Box(
                            modifier = Modifier
                                .fillMaxHeight(barFraction)
                                .width(20.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(barColor)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Day label
                    Text(
                        text = bar.dayLabel,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (bar.isToday) FontWeight.Bold else FontWeight.Normal,
                        color = if (bar.isToday) TextPrimary else TextTertiary,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))
        HorizontalDivider(color = BorderSubtle, thickness = 1.dp)
    }
}
