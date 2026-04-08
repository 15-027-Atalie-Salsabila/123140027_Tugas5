package com.example.notesappnavigation.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.notesappnavigation.navigation.BottomNavItem

@Composable
fun MyBottomBar(navController: NavController) {
    // List menu yang mau ditampilin di bawah
    val items = listOf(
        BottomNavItem.Notes,
        BottomNavItem.Favorites,
        BottomNavItem.Profile
    )

    NavigationBar {
        // Ambil info route mana yang lagi aktif sekarang
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                // Setup icon dari objek BottomNavItem [cite: 284, 295]
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                // Setup tulisan di bawah icon [cite: 285, 305]
                label = { Text(text = item.label) },
                // Cek apakah tab ini lagi dipilih [cite: 300]
                selected = currentRoute == item.route,
                onClick = {
                    // Eksekusi pindah layar [cite: 167, 302]
                    navController.navigate(item.route) {
                        // Opsi biar backstack nggak numpuk pas pindah tab [cite: 192, 303]
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        // Hindari duplicate screen di atas stack [cite: 194, 303]
                        launchSingleTop = true
                        // Balikin state screen kalau sebelumnya udah pernah dibuka [cite: 201, 205]
                        restoreState = true
                    }
                }
            )
        }
    }
}