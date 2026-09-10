package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReelDao {
    @Query("SELECT * FROM daily_reels WHERE date = :date LIMIT 1")
    fun getDailyRecord(date: String): Flow<ReelRecord?>

    @Query("SELECT * FROM daily_reels WHERE date = :date LIMIT 1")
    suspend fun getDailyRecordDirect(date: String): ReelRecord?

    @Query("SELECT * FROM daily_reels ORDER BY date DESC")
    fun getAllRecords(): Flow<List<ReelRecord>>

    @Query("SELECT * FROM daily_reels WHERE date != :today ORDER BY date DESC")
    fun getPastRecords(today: String): Flow<List<ReelRecord>>

    @Query("SELECT * FROM daily_reels ORDER BY date DESC")
    suspend fun getAllRecordsDirect(): List<ReelRecord>

    @Query("SELECT SUM(instagramCount + youtubeCount + tiktokCount) FROM daily_reels")
    fun getLifetimeCount(): Flow<Int?>

    @Query("SELECT SUM(instagramCount) FROM daily_reels")
    fun getLifetimeInstagramCount(): Flow<Int?>

    @Query("SELECT SUM(youtubeCount) FROM daily_reels")
    fun getLifetimeYoutubeCount(): Flow<Int?>

    @Query("SELECT SUM(tiktokCount) FROM daily_reels")
    fun getLifetimeTiktokCount(): Flow<Int?>

    @Query("SELECT COUNT(*) FROM daily_reels WHERE (instagramCount + youtubeCount + tiktokCount) > 0")
    fun getActiveDaysCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(record: ReelRecord)

    @Query("DELETE FROM daily_reels WHERE date = :date")
    suspend fun deleteRecord(date: String)

    @Query("DELETE FROM daily_reels")
    suspend fun clearAll()
}
