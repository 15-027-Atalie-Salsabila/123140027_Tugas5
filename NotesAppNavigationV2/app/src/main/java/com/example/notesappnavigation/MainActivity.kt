package com.example.notesappnavigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.example.notesappnavigation.database.SettingsDataStore
import com.example.notesappnavigation.ui.theme.NotesAppNavigationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val settingsDataStore = remember { SettingsDataStore(applicationContext) }
            val isDarkMode by settingsDataStore.isDarkMode.collectAsState(initial = false)

            NotesAppNavigationTheme(darkTheme = isDarkMode) {
                MainScreen()
            }
        }
    }
}
