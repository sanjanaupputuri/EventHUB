package com.eventhub.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eventhub.app.ui.EventHubApp
import com.eventhub.app.ui.theme.EventHubTheme
import com.eventhub.app.viewmodel.AppViewModel
import com.eventhub.app.viewmodel.ThemeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val appViewModel: AppViewModel = viewModel()
            val themeViewModel: ThemeViewModel = viewModel()
            val currentTheme by themeViewModel.themeMode.collectAsState()

            EventHubTheme(themeMode = currentTheme) {
                EventHubApp(
                    viewModel = appViewModel,
                    themeViewModel = themeViewModel
                )
            }
        }
    }
}
