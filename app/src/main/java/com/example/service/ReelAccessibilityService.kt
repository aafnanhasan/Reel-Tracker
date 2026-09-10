package com.example.service

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.provider.Settings
import android.text.TextUtils
import android.view.accessibility.AccessibilityEvent
import com.example.data.AppDatabase
import com.example.data.ReelRepository
import com.example.model.ReelPlatform
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class ReelAccessibilityService : AccessibilityService() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var repository: ReelRepository? = null

    // 8-Second viewing requirement state
    private var currentPlatform: ReelPlatform? = null
    private var currentWatchJob: Job? = null
    private var hasCountedCurrentReel = false
    private var lastScrollTimestamp = 0L

    override fun onCreate() {
        super.onCreate()
        val db = AppDatabase.getDatabase(applicationContext)
        repository = ReelRepository(db.reelDao())
        _isRunning = true
    }

    override fun onDestroy() {
        super.onDestroy()
        currentWatchJob?.cancel()
        currentWatchJob = null
        _isRunning = false
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        val pkgName = event.packageName?.toString() ?: return
        val platform = ReelPlatform.fromPackageName(pkgName)

        // If user is outside short-form video apps, abort any active timer
        if (platform == null) {
            if (currentPlatform != null) {
                currentWatchJob?.cancel()
                currentWatchJob = null
                currentPlatform = null
                hasCountedCurrentReel = false
            }
            return
        }

        val eventType = event.eventType
        val isScroll = eventType == AccessibilityEvent.TYPE_VIEW_SCROLLED
        val isWindowStateChanged = eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
        val isWindowContent = eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED

        val now = SystemClock.elapsedRealtime()

        // Case 1: User swiped to a new reel (TYPE_VIEW_SCROLLED)
        if (isScroll) {
            // Debounce rapid multi-touch fragments within the same swipe motion
            if (now - lastScrollTimestamp > 400L) {
                lastScrollTimestamp = now
                startNewReelWatchSession(platform)
            }
            return
        }

        // Case 2: Switched into platform or changed screen (TYPE_WINDOW_STATE_CHANGED)
        if (isWindowStateChanged) {
            if (currentPlatform != platform) {
                startNewReelWatchSession(platform)
            }
            return
        }

        // Case 3: Initial playback content loaded and no timer is running yet
        if (isWindowContent && currentWatchJob == null && !hasCountedCurrentReel) {
            startNewReelWatchSession(platform)
        }
    }

    /**
     * Starts an 8-second continuous viewing session for the current reel.
     * If the user swipes away before 8 full seconds, the timer is cancelled and NO reel is counted.
     * Only once 8 continuous seconds are completed without cheating is +1 counted.
     */
    private fun startNewReelWatchSession(platform: ReelPlatform) {
        currentWatchJob?.cancel()
        currentPlatform = platform
        hasCountedCurrentReel = false

        currentWatchJob = serviceScope.launch {
            delay(MIN_WATCH_DURATION_MS)
            if (!hasCountedCurrentReel && currentPlatform == platform) {
                hasCountedCurrentReel = true
                repository?.incrementReel(platform, 1)
                _reelDetectedEvents.emit(platform)
            }
        }
    }

    override fun onInterrupt() {
        currentWatchJob?.cancel()
        currentWatchJob = null
        currentPlatform = null
        hasCountedCurrentReel = false
    }

    companion object {
        const val MIN_WATCH_DURATION_MS = 8000L // 8 full seconds required per reel (no cheating)
        private var _isRunning = false
        val isRunning: Boolean
            get() = _isRunning

        private val _reelDetectedEvents = MutableSharedFlow<ReelPlatform>(extraBufferCapacity = 10)
        val reelDetectedEvents: SharedFlow<ReelPlatform> = _reelDetectedEvents.asSharedFlow()

        fun isAccessibilityEnabled(context: Context): Boolean {
            val expectedServiceName = "${context.packageName}/${ReelAccessibilityService::class.java.name}"
            val enabledServicesSetting = Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            ) ?: return false

            val colonSplitter = TextUtils.SimpleStringSplitter(':')
            colonSplitter.setString(enabledServicesSetting)

            while (colonSplitter.hasNext()) {
                val componentName = colonSplitter.next()
                if (componentName.equals(expectedServiceName, ignoreCase = true)) {
                    return true
                }
            }
            return false
        }

        fun openAccessibilitySettings(context: Context) {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        }
    }
}
