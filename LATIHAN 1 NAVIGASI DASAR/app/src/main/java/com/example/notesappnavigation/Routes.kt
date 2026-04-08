package com.example.notesappnavigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    // Rute detail pake placeholder {noteId} [cite: 216]
    object Detail : Screen("note_detail/{noteId}") {
        fun createRoute(noteId: Int) = "note_detail/$noteId"
    }
}