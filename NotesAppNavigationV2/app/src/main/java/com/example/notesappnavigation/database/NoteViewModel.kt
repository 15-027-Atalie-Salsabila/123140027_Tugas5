package com.example.notesappnavigation.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = NoteRepository(application)
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class, kotlinx.coroutines.FlowPreview::class)
    val notes: StateFlow<List<NoteEntity>> = searchQuery
        .debounce(300L)
        .flatMapLatest { query ->
            _isLoading.value = true
            if (query.isBlank()) repository.getAllNotes()
            else repository.searchNotes(query)
        }
        .onEach { _isLoading.value = false }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoriteNotes: StateFlow<List<NoteEntity>> = repository.getFavoriteNotes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleFavorite(id: Long, currentIsFavorite: Boolean) {
        viewModelScope.launch {
            repository.updateFavorite(id, !currentIsFavorite)
        }
    }

    fun insertNote(title: String, description: String, content: String, reminder: String) {
        viewModelScope.launch {
            repository.insertNote(title, description, content, reminder)
        }
    }

    fun updateNote(id: Long, title: String, description: String, content: String, reminder: String) {
        viewModelScope.launch {
            repository.updateNote(id, title, description, content, reminder)
        }
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch {
            repository.deleteNote(id)
        }
    }
}
