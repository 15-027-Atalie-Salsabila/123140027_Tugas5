package com.example.notesappnavigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.notesappnavigation.ui.theme.NotesAppNavigationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Bungkus pake Theme bawaan biar tampilan Material3 lo cakep
            NotesAppNavigationTheme {
                // Panggil MainScreen sebagai rakitan utama tugas minggu 5
                MainScreen()
            }
        }
    }
}