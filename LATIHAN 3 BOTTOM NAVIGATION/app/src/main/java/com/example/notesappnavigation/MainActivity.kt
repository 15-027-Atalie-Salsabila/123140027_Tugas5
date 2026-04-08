package com.example.notesappnavigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.notesappnavigation.ui.theme.NotesAppNavigationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotesAppNavigationTheme {
                // Panggil MainScreen buat nampilin navigasi lengkap bre!
                MainScreen()
            }
        }
    }
}