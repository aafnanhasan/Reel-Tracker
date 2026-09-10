package com.example.data

data class LifetimeStats(
    val totalReels: Int = 0,
    val instagramTotal: Int = 0,
    val youtubeTotal: Int = 0,
    val tiktokTotal: Int = 0,
    val daysLogged: Int = 0,
    val dailyAverage: Double = 0.0,
    val highestDayCount: Int = 0,
    val highestDayDate: String? = null
)

data class DailyTrendBar(
    val dateString: String,
    val dayLabel: String,
    val count: Int,
    val isToday: Boolean,
    val instagramCount: Int = 0,
    val youtubeCount: Int = 0,
    val tiktokCount: Int = 0
)
