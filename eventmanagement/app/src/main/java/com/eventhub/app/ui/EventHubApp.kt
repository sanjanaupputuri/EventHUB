package com.eventhub.app.ui

import androidx.compose.runtime.*
import com.eventhub.app.data.AppScreen
import com.eventhub.app.ui.screens.*
import com.eventhub.app.viewmodel.AppViewModel
import kotlinx.coroutines.delay

@Composable
fun EventHubApp(viewModel: AppViewModel) {
    LaunchedEffect(Unit) {
        delay(2000)
        viewModel.navigateToScreen(AppScreen.LOGIN)
    }

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
            onUnregisterEvent = { eventId -> viewModel.unregisterFromEvent(eventId) }
        )
    }
}
