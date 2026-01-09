package com.eventhub.app.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.eventhub.app.data.AppScreen
import com.eventhub.app.data.Event
import com.eventhub.app.data.User

class AppViewModel : ViewModel() {
    var currentScreen by mutableStateOf(AppScreen.SPLASH)
        private set
    
    var currentUser by mutableStateOf<User?>(null)
        private set
    
    var events by mutableStateOf(getSampleEvents())
        private set
    
    var registeredEvents by mutableStateOf<Set<String>>(emptySet())
        private set

    fun navigateToScreen(screen: AppScreen) {
        currentScreen = screen
    }

    fun login(user: User) {
        currentUser = user
        currentScreen = AppScreen.MAIN
    }

    fun logout() {
        currentUser = null
        registeredEvents = emptySet()
        currentScreen = AppScreen.LOGIN
    }

    fun registerForEvent(eventId: String) {
        registeredEvents = registeredEvents + eventId
    }

    fun unregisterFromEvent(eventId: String) {
        registeredEvents = registeredEvents - eventId
    }

    private fun getSampleEvents(): List<Event> {
        return listOf(
            Event(
                id = "1",
                title = "Tech Conference 2026",
                description = "Annual technology conference featuring latest innovations",
                date = "2026-02-15",
                time = "09:00 AM",
                location = "Main Auditorium",
                category = "Technology",
                organizer = "Tech Club",
                maxParticipants = 200
            ),
            Event(
                id = "2",
                title = "Cultural Fest",
                description = "Celebrate diversity with music, dance, and food",
                date = "2026-02-20",
                time = "06:00 PM",
                location = "Campus Ground",
                category = "Cultural",
                organizer = "Cultural Committee",
                maxParticipants = 500
            )
        )
    }
}
