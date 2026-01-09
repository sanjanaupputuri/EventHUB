package com.eventhub.app.data

data class User(
    val id: String,
    val name: String,
    val email: String,
    val collegeId: String
)

data class Event(
    val id: String,
    val title: String,
    val description: String,
    val date: String,
    val time: String,
    val location: String,
    val category: String,
    val organizer: String,
    val maxParticipants: Int,
    val currentParticipants: Int = 0
)

enum class AppScreen {
    SPLASH, LOGIN, SIGNUP, MAIN
}
