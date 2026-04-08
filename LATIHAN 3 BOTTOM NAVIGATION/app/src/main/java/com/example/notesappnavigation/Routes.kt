package com.example.notesappnavigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

// Rute buat pindah antar layar biasa [cite: 124]
sealed class Screen(val route: String) {
    object NoteList : Screen("note_list")
    object NoteDetail : Screen("note_detail/{noteId}") {
        fun createRoute(noteId: Int) = "note_detail/$noteId"
    }
}

// Rute khusus buat menu bawah (Bottom Nav) [cite: 282]
sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : BottomNavItem("home_tab", Icons.Default.Home, "Home")
    object Favorites : BottomNavItem("favorites_tab", Icons.Default.Favorite, "Favorites")
    object Profile : BottomNavItem("profile_tab", Icons.Default.Person, "Profile")
}