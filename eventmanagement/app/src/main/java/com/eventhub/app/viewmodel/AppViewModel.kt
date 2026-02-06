package com.eventhub.app.viewmodel

import android.app.Application
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import com.eventhub.app.data.AppScreen
import com.eventhub.app.data.User
import com.eventhub.app.data.mockEvents
import com.eventhub.app.notifications.EventNotificationManager

class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val notificationManager = EventNotificationManager(application)
    
    var currentScreen by mutableStateOf(AppScreen.SPLASH)
        private set
    
    var currentUser by mutableStateOf<User?>(null)
        private set
    
    var events by mutableStateOf(mockEvents)
        private set
    
    var registeredEvents by mutableStateOf<Set<String>>(emptySet())
        private set

    var currentEventIndex by mutableStateOf(0)
        private set
    
    var notificationsEnabled by mutableStateOf(notificationManager.areNotificationsEnabled())
        private set

    fun navigateToScreen(screen: AppScreen) {
        currentScreen = screen
    }

    fun login(user: User) {
        currentUser = user
        currentScreen = AppScreen.MAIN
    }

    fun logout() {
        com.eventhub.app.auth.AuthManager.signOut()
        currentUser = null
        registeredEvents = emptySet()
        currentEventIndex = 0
        currentScreen = AppScreen.LOGIN
    }

    fun registerForEvent(eventId: String) {
        registeredEvents = registeredEvents + eventId
        
        // Schedule reminder if notifications enabled
        events.find { it.id == eventId }?.let {
            notificationManager.scheduleEventNotification(it.id, it.title, it.timestamp)
            // FOR DEMO: Show test notification immediately
            notificationManager.showTestNotification(it.title)
        }
        
        // Move to next event
        val currentIdx = events.indexOfFirst { it.id == eventId }
        if (currentIdx >= 0) {
            for (i in (currentIdx + 1) until events.size) {
                if (events[i].id !in (registeredEvents + eventId)) {
                    currentEventIndex = i
                    return
                }
            }
            for (i in 0 until currentIdx) {
                if (events[i].id !in (registeredEvents + eventId)) {
                    currentEventIndex = i
                    return
                }
            }
        }
    }

    fun unregisterFromEvent(eventId: String) {
        registeredEvents = registeredEvents - eventId
        notificationManager.cancelEventNotifications(eventId)
    }

    fun updateCurrentEventIndex(index: Int) {
        if (events.isEmpty()) return
        
        val direction = if (index >= currentEventIndex) 1 else -1
        var newIndex = index % events.size
        if (newIndex < 0) newIndex += events.size
        
        for (i in 0 until events.size) {
            val checkIndex = if (direction > 0) {
                (newIndex + i) % events.size
            } else {
                (newIndex - i + events.size) % events.size
            }
            
            if (events[checkIndex].id !in registeredEvents) {
                currentEventIndex = checkIndex
                return
            }
        }
    }
    
    // Toggle notifications on/off
    fun toggleNotifications(enabled: Boolean) {
        notificationManager.setNotificationsEnabled(enabled)
        notificationsEnabled = enabled
    }
}
