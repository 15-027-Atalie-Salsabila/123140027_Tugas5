package com.example.notesappnavigation.database

import android.content.Context
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class NoteRepository(context: Context) {
    // Note: Migrating or clearing DB because schema changed (added isFavorite)
    private val driver = AndroidSqliteDriver(NoteDatabase.Schema, context, "notes_v2.db")
    private val database = NoteDatabase(driver)
    private val queries = database.noteEntityQueries

    fun getAllNotes(): Flow<List<NoteEntity>> =
        queries.selectAll().asFlow().mapToList(Dispatchers.IO)

    fun getFavoriteNotes(): Flow<List<NoteEntity>> =
        queries.selectFavorites().asFlow().mapToList(Dispatchers.IO)

    fun searchNotes(query: String): Flow<List<NoteEntity>> =
        queries.searchNotes(query, query).asFlow().mapToList(Dispatchers.IO)

    suspend fun insertNote(title: String, description: String, content: String, reminder: String) {
        queries.insertNote(
            id = null,
            title = title,
            description = description,
            content = content,
            reminder = reminder,
            isFavorite = 0L,
            createdAt = System.currentTimeMillis()
        )
    }

    suspend fun updateNote(id: Long, title: String, description: String, content: String, reminder: String) {
        queries.updateNote(title, description, content, reminder, id)
    }

    suspend fun updateFavorite(id: Long, isFavorite: Boolean) {
        queries.updateFavorite(if (isFavorite) 1L else 0L, id)
    }

    suspend fun deleteNote(id: Long) {
        queries.deleteNote(id)
    }
    
    fun getNoteById(id: Long): NoteEntity? {
        return queries.selectById(id).executeAsOneOrNull()
    }
}
