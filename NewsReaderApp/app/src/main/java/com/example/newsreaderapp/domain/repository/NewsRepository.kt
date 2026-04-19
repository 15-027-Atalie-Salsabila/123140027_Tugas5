package com.example.newsreaderapp.domain.repository

import com.example.newsreaderapp.domain.Article
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getArticles(): Flow<Resource<List<Article>>>
    suspend fun getArticleById(id: Int): Article?
}

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(data: T? = null) : Resource<T>(data)
}
