package com.example.newsreaderapp.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsreaderapp.domain.Article
import com.example.newsreaderapp.domain.repository.NewsRepository
import com.example.newsreaderapp.domain.repository.Resource
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class NewsViewModel(
    private val repository: NewsRepository
) : ViewModel() {

    private val _state = mutableStateOf(NewsState())
    val state: State<NewsState> = _state

    init {
        getArticles()
    }

    fun getArticles() {
        repository.getArticles().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = NewsState(articles = result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _state.value = NewsState(
                        error = result.message ?: "An unexpected error occurred",
                        articles = result.data ?: emptyList()
                    )
                }
                is Resource.Loading -> {
                    _state.value = NewsState(
                        isLoading = true,
                        articles = result.data ?: emptyList()
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}

data class NewsState(
    val isLoading: Boolean = false,
    val articles: List<Article> = emptyList(),
    val error: String = ""
)
