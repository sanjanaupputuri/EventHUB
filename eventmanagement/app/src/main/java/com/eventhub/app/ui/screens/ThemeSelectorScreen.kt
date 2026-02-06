package com.eventhub.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.eventhub.app.ui.theme.EventHubTheme
import com.eventhub.app.ui.theme.ThemeMode
import com.eventhub.app.viewmodel.ThemeViewModel

@Composable
fun ThemeSelectorScreen(
    themeViewModel: ThemeViewModel,
    onBack: () -> Unit = {}
) {
    val currentTheme by themeViewModel.themeMode.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = "Choose Theme",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        ThemeOption(
            title = "Light",
            description = "Clean and bright interface",
            icon = Icons.Default.LightMode,
            isSelected = currentTheme == ThemeMode.LIGHT,
            onClick = { themeViewModel.setThemeMode(ThemeMode.LIGHT) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        ThemeOption(
            title = "Dark",
            description = "Easy on the eyes in low light",
            icon = Icons.Default.DarkMode,
            isSelected = currentTheme == ThemeMode.DARK,
            onClick = { themeViewModel.setThemeMode(ThemeMode.DARK) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        ThemeOption(
            title = "System Default",
            description = "Match device theme settings",
            icon = Icons.Default.PhoneAndroid,
            isSelected = currentTheme == ThemeMode.SYSTEM,
            onClick = { themeViewModel.setThemeMode(ThemeMode.SYSTEM) }
        )
    }
}

@Composable
fun ThemeOption(
    title: String,
    description: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else
                EventHubTheme.extendedColors.cardBackground
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 4.dp else 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isSelected)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = EventHubTheme.extendedColors.textSecondary
                )
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}