package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val username: String = "Alex Scroller",
    val city: String = "New York",
    val country: String = "United States",
    val countryCode: String = "US",
    val dailyBudgetGoal: Int = 60,
    val autoTrackingEnabled: Boolean = true,
    val avatarId: Int = 1,
    val streakShieldsAvailable: Int = 2
)
