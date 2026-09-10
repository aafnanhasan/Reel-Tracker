package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_reels")
data class ReelRecord(
    @PrimaryKey val date: String, // Format: YYYY-MM-DD
    val instagramCount: Int = 0,
    val youtubeCount: Int = 0,
    val tiktokCount: Int = 0,
    val targetReelsGoal: Int = 60,
    val lastUpdatedTimestamp: Long = System.currentTimeMillis()
) {
    val totalCount: Int
        get() = instagramCount + youtubeCount + tiktokCount

    val goalMet: Boolean
        get() = totalCount <= targetReelsGoal
}
