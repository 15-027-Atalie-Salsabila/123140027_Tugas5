package com.example.notesappnavigation.navigation

sealed class Screen(val route: String) {
    object NoteDetail : Screen("note_detail/{noteId}") {
        fun createRoute(noteId: Int) = "note_detail/$noteId"
    }
    object EditNote : Screen("edit_note/{noteId}") {
        fun createRoute(noteId: Int) = "edit_note/$noteId"
    }
    object AddNote : Screen("add_note")
}
