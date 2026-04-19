package com.example.newsreaderapp.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class ArticleDto(
    val id: Int,
    val title: String,
    val body: String
)
