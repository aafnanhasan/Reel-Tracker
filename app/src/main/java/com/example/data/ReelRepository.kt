package com.example.data

import com.example.model.ReelPlatform
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ReelRepository(private val reelDao: ReelDao) {

    private val repoScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val dayLabelFormat = SimpleDateFormat("EEE", Locale.US)

    fun getTodayDateString(): String = dateFormat.format(Date())

    private val _currentDate = MutableStateFlow(getTodayDateString())
    val currentDate: StateFlow<String> = _currentDate.asStateFlow()

    init {
        // Automatically check and trigger midnight (12:00 AM) rollover
        repoScope.launch {
            while (isActive) {
                val now = System.currentTimeMillis()
                val cal = Calendar.getInstance().apply {
                    timeInMillis = now
                    add(Calendar.DAY_OF_MONTH, 1)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 100)
                }
                val delayToMidnight = (cal.timeInMillis - now).coerceAtLeast(1000L)
                val waitTime = minOf(delayToMidnight, 15_000L)
                delay(waitTime)
                checkMidnightRollover()
            }
        }
    }

    fun checkMidnightRollover() {
        val todayStr = getTodayDateString()
        if (_currentDate.value != todayStr) {
            _currentDate.value = todayStr
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val todayRecord: Flow<ReelRecord> = _currentDate
        .flatMapLatest { date ->
            reelDao.getDailyRecord(date).map { record ->
                record ?: ReelRecord(date = date, targetReelsGoal = 60)
            }
        }
        .flowOn(Dispatchers.IO)

    val allRecords: Flow<List<ReelRecord>> = reelDao.getAllRecords()
        .flowOn(Dispatchers.IO)

    @OptIn(ExperimentalCoroutinesApi::class)
    val pastRecords: Flow<List<ReelRecord>> = _currentDate
        .flatMapLatest { date ->
            reelDao.getPastRecords(date)
        }
        .flowOn(Dispatchers.IO)

    val lifetimeStats: Flow<LifetimeStats> = reelDao.getAllRecords().map { records ->
        if (records.isEmpty()) {
            LifetimeStats()
        } else {
            val total = records.sumOf { it.totalCount }
            val ig = records.sumOf { it.instagramCount }
            val yt = records.sumOf { it.youtubeCount }
            val tt = records.sumOf { it.tiktokCount }
            val activeDays = records.count { it.totalCount > 0 }
            val avg = if (activeDays > 0) total.toDouble() / activeDays else 0.0
            val highestRecord = records.maxByOrNull { it.totalCount }

            LifetimeStats(
                totalReels = total,
                instagramTotal = ig,
                youtubeTotal = yt,
                tiktokTotal = tt,
                daysLogged = activeDays,
                dailyAverage = avg,
                highestDayCount = highestRecord?.totalCount ?: 0,
                highestDayDate = highestRecord?.date
            )
        }
    }.flowOn(Dispatchers.IO)

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getRecentDaysTrend(daysCount: Int = 7): Flow<List<DailyTrendBar>> = _currentDate
        .flatMapLatest { todayStr ->
            reelDao.getAllRecords().map { records ->
                val recordMap = records.associateBy { it.date }
                val bars = mutableListOf<DailyTrendBar>()

                // Go back (daysCount - 1) days up to today
                for (i in (daysCount - 1) downTo 0) {
                    val iterCal = Calendar.getInstance()
                    iterCal.add(Calendar.DAY_OF_MONTH, -i)
                    val dStr = dateFormat.format(iterCal.time)
                    val dLabel = dayLabelFormat.format(iterCal.time)
                    val record = recordMap[dStr]
                    val count = record?.totalCount ?: 0

                    bars.add(
                        DailyTrendBar(
                            dateString = dStr,
                            dayLabel = if (dStr == todayStr) "Today" else dLabel,
                            count = count,
                            isToday = dStr == todayStr,
                            instagramCount = record?.instagramCount ?: 0,
                            youtubeCount = record?.youtubeCount ?: 0,
                            tiktokCount = record?.tiktokCount ?: 0
                        )
                    )
                }
                bars
            }
        }
        .flowOn(Dispatchers.IO)

    suspend fun incrementReel(platform: ReelPlatform, amount: Int = 1) = withContext(Dispatchers.IO) {
        checkMidnightRollover()
        val today = getTodayDateString()
        val current = reelDao.getDailyRecordDirect(today) ?: ReelRecord(date = today, targetReelsGoal = 60)

        val updated = when (platform) {
            ReelPlatform.INSTAGRAM -> current.copy(
                instagramCount = (current.instagramCount + amount).coerceAtLeast(0),
                lastUpdatedTimestamp = System.currentTimeMillis()
            )
            ReelPlatform.YOUTUBE -> current.copy(
                youtubeCount = (current.youtubeCount + amount).coerceAtLeast(0),
                lastUpdatedTimestamp = System.currentTimeMillis()
            )
            ReelPlatform.TIKTOK -> current.copy(
                tiktokCount = (current.tiktokCount + amount).coerceAtLeast(0),
                lastUpdatedTimestamp = System.currentTimeMillis()
            )
        }
        reelDao.insertOrUpdate(updated)
    }

    suspend fun setPlatformCount(platform: ReelPlatform, count: Int) = withContext(Dispatchers.IO) {
        checkMidnightRollover()
        val today = getTodayDateString()
        val current = reelDao.getDailyRecordDirect(today) ?: ReelRecord(date = today, targetReelsGoal = 60)
        val safeCount = count.coerceAtLeast(0)

        val updated = when (platform) {
            ReelPlatform.INSTAGRAM -> current.copy(instagramCount = safeCount, lastUpdatedTimestamp = System.currentTimeMillis())
            ReelPlatform.YOUTUBE -> current.copy(youtubeCount = safeCount, lastUpdatedTimestamp = System.currentTimeMillis())
            ReelPlatform.TIKTOK -> current.copy(tiktokCount = safeCount, lastUpdatedTimestamp = System.currentTimeMillis())
        }
        reelDao.insertOrUpdate(updated)
    }

    suspend fun logCustomDay(date: String, ig: Int, yt: Int, tt: Int) = withContext(Dispatchers.IO) {
        val existing = reelDao.getDailyRecordDirect(date)
        val updated = ReelRecord(
            date = date,
            instagramCount = ig.coerceAtLeast(0),
            youtubeCount = yt.coerceAtLeast(0),
            tiktokCount = tt.coerceAtLeast(0),
            targetReelsGoal = existing?.targetReelsGoal ?: 60,
            lastUpdatedTimestamp = System.currentTimeMillis()
        )
        reelDao.insertOrUpdate(updated)
    }

    suspend fun resetToday() = withContext(Dispatchers.IO) {
        checkMidnightRollover()
        val today = getTodayDateString()
        val current = reelDao.getDailyRecordDirect(today) ?: ReelRecord(date = today)
        reelDao.insertOrUpdate(current.copy(instagramCount = 0, youtubeCount = 0, tiktokCount = 0))
    }

    suspend fun clearAllData() = withContext(Dispatchers.IO) {
        reelDao.clearAll()
    }
}
