package com.eventhub.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eventhub.app.ui.EventHubApp
import com.eventhub.app.ui.theme.EventHubTheme
import com.eventhub.app.viewmodel.AppViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EventHubTheme {
                val viewModel: AppViewModel = viewModel()
                EventHubApp(viewModel)
            }
        }
    }
}
