package com.example

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.example.data.LifetimeStats
import com.example.data.ReelRecord
import com.example.ui.components.ReelCounterHero
import com.example.ui.theme.MyApplicationTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [36])
class GreetingScreenshotTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun greeting_screenshot() {
    composeTestRule.setContent {
      MyApplicationTheme {
        ReelCounterHero(
          record = ReelRecord(
            date = "2026-09-10",
            instagramCount = 24,
            youtubeCount = 14,
            tiktokCount = 18,
            targetReelsGoal = 60
          ),
          lifetimeStats = LifetimeStats(
            totalReels = 142,
            instagramTotal = 60,
            youtubeTotal = 40,
            tiktokTotal = 42,
            daysLogged = 5,
            dailyAverage = 28.4
          )
        )
      }
    }

    composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/greeting.png")
  }
}
