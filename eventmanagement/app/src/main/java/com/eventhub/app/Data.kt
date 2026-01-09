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
    val venue: String,
    val category: String,
    val organizer: String,
    val capacity: Int,
    val registeredCount: Int,
    val image: String
)

val mockEvents = listOf(
    Event(
        id = "1",
        title = "Annual Tech Fest 2026",
        description = "Join us for the biggest technical festival of the year featuring hackathons, tech talks, and exciting competitions.",
        date = "Jan 15, 2026",
        time = "9:00 AM - 6:00 PM",
        venue = "Main Auditorium",
        category = "Technical",
        organizer = "Computer Science Department",
        capacity = 500,
        registeredCount = 342,
        image = "https://images.unsplash.com/photo-1540575467063-178a50c2df87"
    ),
    Event(
        id = "2",
        title = "Cultural Night: Rhythm & Beats",
        description = "Experience an evening filled with music, dance, and cultural performances from students across all departments.",
        date = "Jan 20, 2026",
        time = "6:00 PM - 10:00 PM",
        venue = "Open Air Theatre",
        category = "Cultural",
        organizer = "Cultural Committee",
        capacity = 800,
        registeredCount = 654,
        image = "https://images.unsplash.com/photo-1514525253161-7a46d19cd819"
    ),
    Event(
        id = "3",
        title = "Inter-College Basketball Tournament",
        description = "Compete with the best teams from colleges across the city. Show your skills and team spirit.",
        date = "Jan 25-27, 2026",
        time = "8:00 AM - 5:00 PM",
        venue = "Sports Complex",
        category = "Sports",
        organizer = "Sports Department",
        capacity = 200,
        registeredCount = 156,
        image = "https://images.unsplash.com/photo-1546519638-68e109498ffc"
    )
)
