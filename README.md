# EventHUB 🎉

EventHUB is a native Android application designed to simplify **event discovery and management for college students**.

It provides a centralized platform to explore campus events with an **offline-first approach**, ensuring access anytime, anywhere.

---

## 📱 Overview

In most colleges, event information is scattered across WhatsApp groups, posters, and emails.

**EventHUB solves this problem** by offering a single, easy-to-use app where students can:

- Discover upcoming campus events
- Filter events by category and date
- View detailed event information
- Bookmark events for later
- Access previously loaded events even without internet

---

## ✨ Features

### Phase 1 – MVP (Current Scope)

- 🗺️ **Event Dashboard**  
  Displays events in a clean, card-based layout

- 🔍 **Event Filtering**  
  Filter events by category (Tech, Cultural, Sports, Workshops, etc.)

- 🔎 **Search Events**  
  Search events by name or venue

- 📄 **Event Details Screen**  
  View description, date, time, venue, and registration link

- ⭐ **Bookmark Events**  
  Save important events for later reference

- 📱 **Offline-First Support**  
  Previously loaded events remain accessible without internet

---

### Phase 2 – Planned Enhancements

- 🔔 Event reminders & notifications
- 📍 Map view for event locations
- 📊 Live / upcoming event indicators
- 👥 Social features (friends attending)
- 🧾 Event feedback & ratings

---

## 🛠️ Tech Stack

### Language & Platform
- Kotlin
- Android SDK

### Architecture
- MVVM (Model–View–ViewModel)
- Repository Pattern
- Offline-First Design

### Libraries & Tools
- Jetpack Compose – UI
- Material Design 3
- Room Database – Local storage
- Retrofit – API communication
- Hilt – Dependency Injection
- Kotlin Coroutines & Flow

---

## 📁 Project Structure

```
EventHUB/
│
├── eventmanagement/
│   └── app/
│       ├── src/
│       │   ├── main/
│       │   │   ├── java/com/eventhub/
│       │   │   │   ├── data/
│       │   │   │   ├── ui/
│       │   │   │   ├── viewmodel/
│       │   │   │   └── MainActivity.kt
│       │   │   └── res/
│       │   └── AndroidManifest.xml
│       └── build.gradle.kts
│
├── docs/
│   └── project_documentation.md
│
├── README.md
└── .gitignore
```

---

## 🚀 Getting Started

### Prerequisites
- Android Studio (Hedgehog or later)
- JDK 11+
- Android SDK (API 24+)

### Setup Instructions

1. Clone the repository
   ```bash
   git clone https://github.com/sanjanaupputuri/EventHUB.git

2. Open the project in Android Studio

3. Sync Gradle files

4. Run the app on an emulator or physical device
   
---

## 👩‍💻 Development Team

- **Team Size:** 3  
- **Project Duration:** 3 Weeks  
- **Project Type:** Academic Mini / Capstone Project  

### 👥 Team Members

- **Member 1:** Balagam Risha Raj  
  - GitHub: https://github.com/balagamrisha  

- **Member 2:** Sanjana Upputuri  
  - GitHub: https://github.com/sanjanaupputuri  

- **Member 3:** Sameeksha  
  - GitHub: https://github.com/SameekshaB656  

---

### 📅 Development Roadmap


**Week 1**
- Project setup
- Architecture implementation
- Home & Event Details UI
- Local database setup

**Week 2**
- Event filtering & search
- Offline caching
- Bookmark functionality

**Week 3**
- UI polishing
- Bug fixes
- Final testing & submission

---

### 📄 License

This project is developed for academic purposes only.

All rights reserved to the development team.

---

**⭐ EventHUB – Built by students, for students**
