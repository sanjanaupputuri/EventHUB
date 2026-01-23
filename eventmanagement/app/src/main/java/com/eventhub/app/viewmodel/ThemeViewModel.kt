package com.eventhub.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eventhub.app.ui.theme.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ThemeViewModel : ViewModel() {

    // Default theme is LIGHT
    private val _themeMode = MutableStateFlow(ThemeMode.LIGHT)
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            _themeMode.value = mode
            // You can save to DataStore/SharedPreferences here
            // saveThemePreference(mode)
        }
    }

    fun toggleTheme() {
        viewModelScope.launch {
            _themeMode.value = when (_themeMode.value) {
                ThemeMode.LIGHT -> ThemeMode.DARK
                ThemeMode.DARK -> ThemeMode.LIGHT
                ThemeMode.SYSTEM -> ThemeMode.LIGHT
            }
        }
    }

    // Optional: Load saved theme preference
    /*
    private fun loadThemePreference() {
        viewModelScope.launch {
            // Load from DataStore/SharedPreferences
            val savedTheme = dataStore.data.map { it[THEME_KEY] ?: ThemeMode.LIGHT.name }
            savedTheme.collect { themeName ->
                _themeMode.value = ThemeMode.valueOf(themeName)
            }
        }
    }

    private suspend fun saveThemePreference(mode: ThemeMode) {
        // Save to DataStore/SharedPreferences
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = mode.name
        }
    }
    */
}