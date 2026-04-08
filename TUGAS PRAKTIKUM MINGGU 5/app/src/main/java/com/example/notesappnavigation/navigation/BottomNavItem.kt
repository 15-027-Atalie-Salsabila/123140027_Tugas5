package com.example.notesappnavigation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Notes : BottomNavItem("notes_tab", Icons.AutoMirrored.Filled.List, "Notes")
    object Favorites : BottomNavItem("favorites_tab", Icons.Default.Favorite, "Favorites")
    object Profile : BottomNavItem("profile_tab", Icons.Default.Person, "Profile")
}