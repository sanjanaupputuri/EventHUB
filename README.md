# College Event Manager

## 1. Problem Statement

Educational institutions face significant challenges in event coordination and communication:

- **Fragmented Communication**: Event announcements scattered across WhatsApp groups, notice boards, and emails lead to missed opportunities and poor student engagement
- **Manual Registration**: Paper-based or spreadsheet registration systems are time-consuming, error-prone, and lack real-time visibility
- **Administrative Overhead**: Organizers spend excessive time on logistics rather than event quality; admins lack centralized oversight of campus activities
- **Student Disconnect**: Students miss relevant events due to information overload and absence of personalized filtering mechanisms

The core challenge is creating a **mobile-native bridge** between institutional event management systems and student participation, enabling seamless event lifecycle management from administrative creation to student attendance.

---

## 2. Idea Description

**App Name**: **EventHub**

EventHub is a mobile-first event engagement platform that transforms how colleges manage and students discover campus activities. Unlike traditional web-based management systems that serve administrative needs, EventHub focuses on student experience while integrating with existing institutional infrastructure.

**Core Innovation**: API-driven architecture that consumes event data from the college's web management system, eliminating duplicate data entry while providing students with a native mobile experience optimized for browsing, registration, and real-time updates.

**Value Proposition**:
- **For Students**: Centralized platform for discovering all campus events with personalized recommendations and streamlined registration
- **For Organizers**: Increased event visibility and attendee engagement with real-time registration tracking and automated participant communication
- **For Institution**: Enhanced student engagement metrics and seamless integration with existing systems

---

## 3. List of Base Features

### 3.1 Core Features
*Estimated Development Time: 8-10 weeks*

**API Integration & Event Sync**
- REST API endpoint consumption from college web system
- JSON payload parsing for event creation/updates
- Automatic sync mechanism (polling/webhook-based)
- Data validation and error handling for malformed API responses

**User Authentication & Roles**
- Firebase Authentication (Email/Password)
- Student account creation and login
- Profile management with college ID verification
- Secure session management

**Event Discovery (Students)**
- Feed view of upcoming approved events
- Category-based filtering (Cultural, Technical, Sports, Workshop, Seminar)
- Date range filtering and search functionality
- Event detail view with poster, description, venue, timings

**Event Registration**
- One-tap registration with capacity management
- Registration confirmation with QR code generation
- "My Events" dashboard showing registered events
- Registration cancellation with deadline enforcement

**Push Notifications**
- FCM integration for event announcements
- Registration confirmations and reminders (24hr before event)
- Event updates and cancellation notifications

**Organizer Dashboard**
- View registered participants list for owned events
- Real-time registration count display
- Basic analytics (registration trend graph)

**Admin Panel**
- View all events (pending, approved, rejected)
- Approve/reject event proposals with feedback notes
- User role management interface

**Offline Support**
- Local caching of event data using Room Database
- Offline browsing of previously loaded events
- Queue sync for actions performed offline

### 3.2 Enhanced Features
*To be implemented post-MVP based on bandwidth*

**Enhanced Discovery**
- Personalized event recommendations based on past registrations
- Favorites/bookmark functionality
- Calendar view with monthly event overview
- Share events to social media/WhatsApp

**Advanced Organizer Tools**
- QR code scanner for attendance marking
- Export participant lists to CSV
- Capacity utilization alerts
- Post-event feedback collection

**Gamification**
- Student engagement leaderboard
- Badges for event attendance milestones
- Department-wise participation rankings

**Communication**
- In-app chat for event-specific queries
- Broadcast messaging for organizers
- Announcement section for admin-wide notices

**Analytics Dashboard**
- Admin analytics: college-wide event trends, peak periods
- Organizer analytics: demographic breakdown of attendees
- Student analytics: events attended history with certificates

### 3.2 Future Considerations
*Features intentionally deferred to maintain focused scope and faster time-to-market*

**Event Payment Integration**: Financial transaction processing will continue through the college's existing web system. EventHub will display event cost and payment status, with payment links redirecting to the institutional portal.

**Live Streaming Integration**: Event video streaming will leverage external platforms (YouTube Live, Zoom). EventHub will provide links to these platforms rather than building native streaming capabilities.

**Event Creation via Mobile**: To maintain data integrity and leverage existing administrative workflows, event creation remains exclusive to the college's web management system. Mobile app focuses on consumption and engagement rather than content authoring.

**Community Interaction Features**: User-generated content such as event reviews, photo galleries, comment threads, and social sharing will be evaluated based on moderation requirements and resource availability in future iterations.

---

## 4. Tech Stack

| Layer | Technology | Justification |
|-------|-----------|---------------|
| **Language** | Kotlin | Modern, concise, official Android language with null safety |
| **UI Framework** | Jetpack Compose | Declarative UI for faster development and Material Design 3 support |
| **Architecture** | MVVM + Clean Architecture | Separation of concerns, testability, scalability |
| **Backend** | Firebase Suite | - **Firestore**: Real-time NoSQL database<br>- **Auth**: Managed authentication<br>- **Storage**: Event poster images<br>- **FCM**: Push notifications |
| **Networking** | Retrofit + OkHttp | Type-safe REST API consumption for college system integration |
| **Local Database** | Room | Offline caching and SQLite abstraction |
| **Async Operations** | Kotlin Coroutines + Flow | Non-blocking operations and reactive data streams |
| **Image Loading** | Coil | Efficient image loading optimized for Compose |
| **Dependency Injection** | Hilt | Compile-time DI for better performance |
| **Navigation** | Navigation Component | Type-safe navigation with argument passing |
| **Testing** | JUnit, MockK, Espresso | Unit, integration, and UI testing |
| **CI/CD** | GitHub Actions | Automated builds and testing |

**Minimum SDK**: API 24 (Android 7.0 - 94% market coverage)  
**Target SDK**: API 34 (Android 14)

---

## 5. Notes

### Collaboration Partnership
EventHub is being developed in coordination with an existing college digital ecosystem platform that handles student networking, resource sharing, and academic utilities. This collaboration enables:

- **Single Sign-On (SSO)**: Students can use existing credentials from the partner app to access EventHub
- **Cross-Platform Notifications**: Event reminders can be delivered through the partner app's notification infrastructure
- **Unified Student Profiles**: Attendance records and participation data will be accessible across both platforms

*Technical integration details will be finalized during Phase 2 development based on partner API specifications.*

### Development Approach
- **API-First Design**: All features will be built assuming event data originates from the college web system; manual event creation via mobile app is intentionally excluded to maintain single source of truth
- **Modular Architecture**: Each feature module (auth, events, notifications) will be independently testable and deployable
- **Phased Rollout**: Pilot launch with single department before college-wide deployment to gather feedback

### Security Considerations
- All API communication will use HTTPS with certificate pinning
- Sensitive data (student IDs, contact info) will be encrypted at rest
- Firebase Security Rules will enforce role-based data access

### Scalability Targets
- Support for 5,000+ concurrent users (typical large college enrollment)
- Handle 50+ events per month with 500+ registrations per event
- Real-time sync latency under 2 seconds for critical updates
