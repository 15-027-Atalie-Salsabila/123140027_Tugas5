package com.example.notesappnavigation.database

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsDataStore(private val context: Context) {
    companion object {
        val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
        val SORT_ORDER_KEY = stringPreferencesKey("sort_order")
    }

    val isDarkMode: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[DARK_MODE_KEY] ?: false
    }

    val sortOrder: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[SORT_ORDER_KEY] ?: "newest"
    }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DARK_MODE_KEY] = enabled
        }
    }

    suspend fun setSortOrder(order: String) {
        context.dataStore.edit { preferences ->
            preferences[SORT_ORDER_KEY] = order
        }
    }
}
