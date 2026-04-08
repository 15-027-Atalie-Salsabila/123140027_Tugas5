package com.example.notesappnavigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.NoteList.route // Start dari list [cite: 410]
    ) {
        // Layar Daftar Catatan
        composable(Screen.NoteList.route) {
            NoteListScreen(onNoteClick = { id ->
                navController.navigate(Screen.NoteDetail.createRoute(id)) {
                    launchSingleTop = true // Biar nggak stack putih kayak tadi bre
                }
            })
        }

        // Layar Detail dengan argument [cite: 411]
        composable(
            route = Screen.NoteDetail.route,
            arguments = listOf(
                navArgument("noteId") { type = NavType.IntType } // navArgument setup [cite: 413, 415]
            )
        ) { backStackEntry ->
            // Ambil noteId dari rute [cite: 416]
            val noteId = backStackEntry.arguments?.getInt("noteId") ?: 0
            NoteDetailScreen(noteId = noteId, onBack = {
                navController.popBackStack()
            })
        }
    }
}