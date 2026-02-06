package com.eventhub.app.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.eventhub.app.MainActivity
import com.eventhub.app.R
import java.util.concurrent.TimeUnit

class EventNotificationManager(private val context: Context) {
    
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val prefs = context.getSharedPreferences("notifications", Context.MODE_PRIVATE)
    
    companion object {
        const val CHANNEL_ID = "event_reminders"
        const val CHANNEL_NAME = "Event Reminders"
        const val PREF_NOTIFICATIONS_ENABLED = "notifications_enabled"
    }
    
    init {
        createNotificationChannel()
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for upcoming events"
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    // Check if notifications are enabled
    fun areNotificationsEnabled(): Boolean {
        return prefs.getBoolean(PREF_NOTIFICATIONS_ENABLED, true) // Default: ON
    }
    
    // Toggle notifications on/off
    fun setNotificationsEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(PREF_NOTIFICATIONS_ENABLED, enabled).apply()
        
        if (!enabled) {
            // Cancel all scheduled notifications
            WorkManager.getInstance(context).cancelAllWorkByTag("event_notification")
        }
    }
    
    // Schedule reminder 1 day before event
    fun scheduleEventNotification(eventId: String, eventTitle: String, eventTime: Long) {
        if (!areNotificationsEnabled()) return // Don't schedule if disabled
        
        val oneDayBefore = eventTime - (24 * 60 * 60 * 1000)
        val delayMillis = oneDayBefore - System.currentTimeMillis()
        
        if (delayMillis <= 0) return
        
        val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
            .setInputData(
                workDataOf(
                    "eventId" to eventId,
                    "eventTitle" to eventTitle
                )
            )
            .addTag("event_notification")
            .addTag("event_$eventId")
            .build()
        
        WorkManager.getInstance(context).enqueue(workRequest)
    }
    
    // TEST FUNCTION - Show notification immediately for demo
    fun showTestNotification(eventTitle: String) {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Event Tomorrow!")
            .setContentText("$eventTitle is happening tomorrow")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
        
        notificationManager.notify(eventTitle.hashCode(), notification)
    }
    
    // Cancel notification when unregistered
    fun cancelEventNotifications(eventId: String) {
        WorkManager.getInstance(context).cancelAllWorkByTag("event_$eventId")
    }
}

// Worker for scheduled reminders
class NotificationWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    
    override fun doWork(): Result {
        val eventTitle = inputData.getString("eventTitle") ?: return Result.failure()
        val eventId = inputData.getString("eventId") ?: return Result.failure()
        
        val intent = Intent(applicationContext, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext, eventId.hashCode(), intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        
        val notification = NotificationCompat.Builder(applicationContext, EventNotificationManager.CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Event Tomorrow!")
            .setContentText("$eventTitle is happening tomorrow")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
        
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(eventId.hashCode(), notification)
        
        return Result.success()
    }
}
