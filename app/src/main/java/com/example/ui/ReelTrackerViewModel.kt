package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.DailyTrendBar
import com.example.data.LifetimeStats
import com.example.data.ReelRecord
import com.example.data.ReelRepository
import com.example.model.ReelPlatform
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ReelTrackerViewModel(
    application: Application,
    private val repository: ReelRepository
) : AndroidViewModel(application) {

    val currentDate: StateFlow<String> = repository.currentDate

    val todayRecord: StateFlow<ReelRecord> = repository.todayRecord
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ReelRecord(date = repository.getTodayDateString(), targetReelsGoal = 60)
        )

    val pastRecords: StateFlow<List<ReelRecord>> = repository.pastRecords
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val lifetimeStats: StateFlow<LifetimeStats> = repository.lifetimeStats
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = LifetimeStats()
        )

    val weeklyTrend: StateFlow<List<DailyTrendBar>> = repository.getRecentDaysTrend(7)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun refreshDate() {
        repository.checkMidnightRollover()
    }

    fun incrementReel(platform: ReelPlatform, amount: Int = 1) {
        viewModelScope.launch {
            repository.incrementReel(platform, amount)
        }
    }

    fun setPlatformCount(platform: ReelPlatform, count: Int) {
        viewModelScope.launch {
            repository.setPlatformCount(platform, count)
        }
    }

    fun resetToday() {
        viewModelScope.launch {
            repository.resetToday()
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            repository.clearAllData()
        }
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val db = AppDatabase.getDatabase(application)
            val repo = ReelRepository(db.reelDao())
            return ReelTrackerViewModel(application, repo) as T
        }
    }
}
