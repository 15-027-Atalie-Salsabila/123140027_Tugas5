package com.example.notesappnavigation

sealed class Screen(val route: String) {
    object NoteList : Screen("note_list") // [cite: 406]
    object NoteDetail : Screen("note_detail/{noteId}") { //
        fun createRoute(noteId: Int) = "note_detail/$noteId" // [cite: 408]
    }
}