# UNIS App - Implementation Guide

## 🔥 Firebase Setup Required

### Step 1: Enable Firestore in Firebase Console
1. Go to https://console.firebase.google.com
2. Select your project (EventHub/UNIS)
3. Click **Firestore Database** in left menu
4. Click **Create Database**
5. Choose **Start in test mode** (for development)
6. Select your region (closest to you)
7. Click **Enable**

### Step 2: Update Firestore Rules (Security)
In Firestore console, go to **Rules** tab and paste:

```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Allow users to read/write their own registrations
    match /registrations/{document} {
      allow read: if request.auth != null;
      allow write: if request.auth != null && 
                     request.resource.data.userId == request.auth.uid;
      allow delete: if request.auth != null && 
                      resource.data.userId == request.auth.uid;
    }
  }
}
```

### Step 3: Add Notification Permission to AndroidManifest.xml
Add this inside `<manifest>` tag:

```xml
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
<uses-permission android:name="android.permission.SCHEDULE_EXACT_ALARM" />
```

---

## 📁 Architecture Overview

### Current Setup (Mock API)
```
MockEventRepository
  ├── Events: From mockEvents list (simulates API)
  └── Registrations: Stored in Firebase Firestore (persists on logout)
```

### Future Setup (Real API)
```
APIEventRepository
  ├── Events: Fetched from your web team's API
  └── Registrations: Synced with API + Firestore backup
```

---

## 🔄 How to Switch from Mock to Real API

When your web team provides the API, change **ONE LINE** in your ViewModel:

**Before (Mock):**
```kotlin
private val repository: EventRepository = MockEventRepository()
```

**After (Real API):**
```kotlin
private val repository: EventRepository = APIEventRepository("https://your-api-url.com")
```

That's it! Everything else works automatically.

---

## 📊 What Each File Does

### Data Layer
- **Models.kt** - Data structures (Event, User, Registration)
- **MockData.kt** - Fake events with poster URLs and timestamps
- **EventRepository.kt** - Interface (contract) for data operations
- **MockEventRepository.kt** - Current implementation (Firestore + mock events)
- **APIEventRepository.kt** - Future implementation (ready for real API)

### Notifications
- **EventNotificationManager.kt** - Schedules reminders (24h & 1h before events)
- Uses WorkManager for reliable background scheduling

---

## 🔔 Notification System

### Features
1. **New Event Notification** - When moderator adds event via web interface
2. **24-Hour Reminder** - Day before registered event
3. **1-Hour Reminder** - Hour before registered event

### How It Works
- When user registers for event → Schedules 2 notifications
- When user unregisters → Cancels scheduled notifications
- Notifications work even if app is closed (WorkManager)

---

## 💾 Data Persistence Solution

### Problem: Data wiped on logout
**Cause:** Events stored only in memory (ViewModel state)

### Solution: Firebase Firestore
- User registrations saved to cloud database
- Survives app restart, logout, device reboot
- Real-time sync across devices

### How It Works Now:
1. User registers for event → Saved to Firestore
2. User logs out → Data stays in Firestore
3. User logs back in → Registrations loaded from Firestore
4. Events displayed correctly in "My Events"

---

## 🌐 API Integration Plan

### What Your Web Team Needs to Provide

**Base URL:** `https://your-college-api.com/api/v1`

**Endpoints Required:**

1. **GET /events**
   - Returns list of all events
   - Response format:
   ```json
   [
     {
       "id": "evt_123",
       "title": "Tech Fest 2026",
       "description": "...",
       "date": "2026-01-15",
       "time": "09:00",
       "location": "Main Auditorium",
       "category": "Technical",
       "organizer": "CS Dept",
       "maxParticipants": 500,
       "currentParticipants": 342,
       "posterUrl": "https://cdn.college.com/posters/evt_123.jpg",
       "timestamp": 1705305600000
     }
   ]
   ```

2. **POST /registrations/{userId}/{eventId}**
   - Registers user for event
   - Returns success/error

3. **DELETE /registrations/{userId}/{eventId}**
   - Unregisters user from event
   - Returns success/error

4. **GET /registrations/{userId}**
   - Returns list of event IDs user is registered for
   - Response: `["evt_123", "evt_456"]`

---

## 🚀 Next Steps

### Phase 1: Current (Mock Data) ✅
- [x] Firebase Firestore for registrations
- [x] Notification system
- [x] Repository pattern
- [ ] Update ViewModel to use repository
- [ ] Test notifications
- [ ] Request notification permission at runtime

### Phase 2: Offline Support (Optional)
- [ ] Add Room database for local caching
- [ ] Sync events to local DB
- [ ] Show cached events when offline
- [ ] Queue registration actions when offline

### Phase 3: API Integration (When Ready)
- [ ] Get API URL from web team
- [ ] Test API endpoints with Postman
- [ ] Switch to APIEventRepository
- [ ] Test end-to-end flow
- [ ] Handle API errors gracefully

---

## 🧪 Testing Checklist

### Test Data Persistence
1. Login → Register for events
2. Logout
3. Login again
4. ✅ Events should still be in "My Events"

### Test Notifications
1. Register for event with timestamp in future
2. Check WorkManager scheduled tasks
3. Wait for notification (or change time for testing)
4. ✅ Notification should appear

### Test Offline (Future)
1. Turn off internet
2. Open app
3. ✅ Should show cached events
4. Register for event
5. Turn on internet
6. ✅ Registration should sync

---

## 📝 Important Notes

### For Development
- Mock data has realistic timestamps (7-41 days from now)
- Poster URLs are placeholders - replace with real CDN links
- Test mode Firestore rules allow all authenticated users

### For Production
- Update Firestore rules to be more restrictive
- Add error handling for API failures
- Implement retry logic for failed syncs
- Add analytics for event registrations
- Set up Firebase Cloud Messaging for push notifications

### For Your Web Team
- Share the API endpoint specifications above
- Event IDs must be unique and consistent
- Timestamps should be in milliseconds (Unix epoch)
- Poster URLs should be publicly accessible
- Consider rate limiting and authentication

---

## 🆘 Troubleshooting

**Registrations not persisting?**
- Check Firebase console → Firestore → registrations collection
- Verify user is authenticated (userId exists)
- Check Firestore rules allow write access

**Notifications not working?**
- Check notification permission granted
- Verify WorkManager tasks scheduled (Android Studio → App Inspection → WorkManager)
- Test with shorter delays (5 minutes instead of 24 hours)

**API not connecting?**
- Verify base URL is correct
- Check internet permission in manifest
- Test API with Postman first
- Check API returns correct JSON format

---

## 📞 Questions?

If you need help with:
- Firebase setup → Check Firebase docs or ask me
- API integration → Coordinate with web team
- Notification testing → Use WorkManager inspector
- Architecture questions → Review repository pattern docs
