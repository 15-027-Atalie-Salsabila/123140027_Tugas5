package com.example.notesappnavigation.database

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class RemoteNote(
    val id: Long? = null,
    val title: String,
    val description: String,
    val content: String,
    val reminder: String
)

class NoteApi {
    private val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    // Mock endpoint - User should replace with actual URL
    private val BASE_URL = "https://my-json-server.typicode.com/example/notes"

    suspend fun getNotes(): List<RemoteNote> {
        return try {
            client.get("$BASE_URL/notes").body()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun pushNote(note: RemoteNote) {
        try {
            client.post("$BASE_URL/notes") {
                setBody(note)
            }
        } catch (e: Exception) {
            // Log error
        }
    }
}
