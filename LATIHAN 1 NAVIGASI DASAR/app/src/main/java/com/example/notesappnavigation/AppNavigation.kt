package com.example.notesappnavigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun AppNavigation() {
    val navController = rememberNavController() // Setup NavController [cite: 147]

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route // Start rute awal [cite: 152]
    ) {
        // Route Home [cite: 154]
        composable(Screen.Home.route) {
            HomeScreen(onNavigateToDetail = { id ->
                navController.navigate(Screen.Detail.createRoute(id)) {
                    launchSingleTop = true
                }
            })
        }

        // Route Detail dengan argument Int [cite: 219, 220]
        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("noteId") { type = NavType.IntType } // Tipe data Integer [cite: 221, 255]
            )
        ) { backStackEntry ->
            // Ambil data dari backStackEntry [cite: 222, 223]
            val noteId = backStackEntry.arguments?.getInt("noteId") ?: 0
            DetailScreen(
                noteId = noteId,
                onBack = { navController.popBackStack() } // Navigasi balik [cite: 163, 170]
            )
        }
    }
}