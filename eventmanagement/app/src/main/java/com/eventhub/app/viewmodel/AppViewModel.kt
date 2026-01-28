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
                title = "Annual Tech Fest 2026",
                description = "Join us for the biggest technical festival of the year featuring hackathons, tech talks, and exciting competitions.",
                date = "Jan 15, 2026",
                time = "9:00 AM - 6:00 PM",
                location = "Main Auditorium",
                category = "Technical",
                organizer = "Computer Science Department",
                maxParticipants = 500,
                currentParticipants = 342
            ),
            Event(
                id = "2",
                title = "Cultural Night: Rhythm & Beats",
                description = "Experience an evening filled with music, dance, and cultural performances from students across all departments.",
                date = "Jan 20, 2026",
                time = "6:00 PM - 10:00 PM",
                location = "Open Air Theatre",
                category = "Cultural",
                organizer = "Cultural Committee",
                maxParticipants = 800,
                currentParticipants = 654
            ),
            Event(
                id = "3",
                title = "Inter-College Basketball Tournament",
                description = "Compete with the best teams from colleges across the city. Show your skills and team spirit.",
                date = "Jan 25-27, 2026",
                time = "8:00 AM - 5:00 PM",
                location = "Sports Complex",
                category = "Sports",
                organizer = "Sports Department",
                maxParticipants = 200,
                currentParticipants = 156
            ),
            Event(
                id = "4",
                title = "AI & Machine Learning Workshop",
                description = "Learn the fundamentals of AI and ML with hands-on projects and expert guidance from industry professionals.",
                date = "Feb 2, 2026",
                time = "10:00 AM - 4:00 PM",
                location = "Computer Lab 1",
                category = "Workshop",
                organizer = "Tech Club",
                maxParticipants = 50,
                currentParticipants = 35
            ),
            Event(
                id = "5",
                title = "Photography Exhibition",
                description = "Showcase your creativity through the lens. Submit your best shots and compete for amazing prizes.",
                date = "Feb 8, 2026",
                time = "11:00 AM - 7:00 PM",
                location = "Art Gallery",
                category = "Cultural",
                organizer = "Photography Club",
                maxParticipants = 300,
                currentParticipants = 89
            ),
            Event(
                id = "6",
                title = "Startup Pitch Competition",
                description = "Present your innovative business ideas to a panel of investors and entrepreneurs. Win funding for your startup!",
                date = "Feb 12, 2026",
                time = "2:00 PM - 6:00 PM",
                location = "Business Incubator",
                category = "Business",
                organizer = "Entrepreneurship Cell",
                maxParticipants = 100,
                currentParticipants = 67
            ),
            Event(
                id = "7",
                title = "Gaming Tournament: Esports Arena",
                description = "Battle it out in popular games like Valorant, CS2, and FIFA. Form teams and compete for the championship.",
                date = "Feb 15, 2026",
                time = "12:00 PM - 8:00 PM",
                location = "Gaming Zone",
                category = "Gaming",
                organizer = "Gaming Club",
                maxParticipants = 128,
                currentParticipants = 98
            ),
            Event(
                id = "8",
                title = "Environmental Awareness Drive",
                description = "Join us in making our campus greener. Tree plantation, waste management workshop, and sustainability talks.",
                date = "Feb 18, 2026",
                time = "8:00 AM - 2:00 PM",
                location = "Campus Grounds",
                category = "Social",
                organizer = "Green Club",
                maxParticipants = 200,
                currentParticipants = 145
            )
        )
    }
}
