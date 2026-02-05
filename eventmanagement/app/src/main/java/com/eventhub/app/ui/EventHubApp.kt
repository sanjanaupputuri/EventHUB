package com.eventhub.app.ui

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.eventhub.app.auth.AuthManager
import com.eventhub.app.data.AppScreen
import com.eventhub.app.data.Event
import com.eventhub.app.data.User
import com.eventhub.app.ui.screens.*
import com.eventhub.app.viewmodel.AppViewModel
import com.eventhub.app.viewmodel.ThemeViewModel
import kotlinx.coroutines.delay

@Composable
fun EventHubApp(
    viewModel: AppViewModel,
    themeViewModel: ThemeViewModel
) {
    val context = LocalContext.current
    var showEventDetails by remember { mutableStateOf(false) }
    var selectedEvent by remember { mutableStateOf<Event?>(null) }
    
    LaunchedEffect(Unit) {
        AuthManager.initialize(context)
        delay(2000)
        
        // Check if user is already logged in
        if (AuthManager.isLoggedIn()) {
            val email = AuthManager.getCurrentUserEmail() ?: ""
            val rollNumber = email.substringBefore("@")
            val user = User(rollNumber, rollNumber, email, rollNumber)
            viewModel.login(user)
            viewModel.navigateToScreen(AppScreen.MAIN)
        } else {
            viewModel.navigateToScreen(AppScreen.LOGIN)
        }
    }

    if (showEventDetails && selectedEvent != null) {
        EventDetailsScreen(
            event = selectedEvent!!,
            onBack = {
                showEventDetails = false
                selectedEvent = null
            },
            onRegister = {
                viewModel.registerForEvent(selectedEvent!!.id)
                showEventDetails = false
                selectedEvent = null
            }
        )
    } else {
        when (viewModel.currentScreen) {
            AppScreen.SPLASH -> SplashScreen()
            AppScreen.LOGIN -> LoginScreen(
                onLogin = { user -> viewModel.login(user) },
                onNavigateToSignup = { viewModel.navigateToScreen(AppScreen.SIGNUP) }
            )
            AppScreen.SIGNUP -> SignupScreen(
                onSignup = { user -> viewModel.login(user) },
                onNavigateToLogin = { viewModel.navigateToScreen(AppScreen.LOGIN) }
            )
            AppScreen.MAIN -> MainApp(
                user = viewModel.currentUser!!,
                events = viewModel.events,
                registeredEvents = viewModel.registeredEvents,
                currentEventIndex = viewModel.currentEventIndex,
                onLogout = { viewModel.logout() },
                onRegisterEvent = { eventId -> viewModel.registerForEvent(eventId) },
                onUnregisterEvent = { eventId -> viewModel.unregisterFromEvent(eventId) },
                onUpdateEventIndex = { index -> viewModel.updateCurrentEventIndex(index) },
                themeViewModel = themeViewModel,
                onShowEventDetails = { event ->
                    selectedEvent = event
                    showEventDetails = true
                }
            )
        }
    }
}