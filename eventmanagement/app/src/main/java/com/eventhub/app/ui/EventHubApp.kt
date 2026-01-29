package com.eventhub.app.ui

import androidx.compose.runtime.*
import com.eventhub.app.data.AppScreen
import com.eventhub.app.data.Event
import com.eventhub.app.ui.screens.*
import com.eventhub.app.viewmodel.AppViewModel
import com.eventhub.app.viewmodel.ThemeViewModel
import kotlinx.coroutines.delay

@Composable
fun EventHubApp(
    viewModel: AppViewModel,
    themeViewModel: ThemeViewModel
) {
    var showEventDetails by remember { mutableStateOf(false) }
    var selectedEvent by remember { mutableStateOf<Event?>(null) }
    
    LaunchedEffect(Unit) {
        delay(2000)
        viewModel.navigateToScreen(AppScreen.LOGIN)
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
                onLogout = { viewModel.logout() },
                onRegisterEvent = { eventId -> viewModel.registerForEvent(eventId) },
                onUnregisterEvent = { eventId -> viewModel.unregisterFromEvent(eventId) },
                themeViewModel = themeViewModel,
                onShowEventDetails = { event ->
                    selectedEvent = event
                    showEventDetails = true
                }
            )
        }
    }
}