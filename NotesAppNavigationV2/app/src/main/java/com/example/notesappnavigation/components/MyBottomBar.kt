package com.example.notesappnavigation.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.notesappnavigation.navigation.BottomNavItem

@Composable
fun MyBottomBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Notes,
        BottomNavItem.Favorites,
        BottomNavItem.Profile
    )

    NavigationBar(
        containerColor = Color(0xFFF8F4F8)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = { Text(text = item.label) },
                selected = currentRoute == item.route,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color(0xFFE8DEF8),
                    selectedIconColor = Color(0xFF1D1B20),
                    unselectedIconColor = Color(0xFF49454F)
                ),
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}