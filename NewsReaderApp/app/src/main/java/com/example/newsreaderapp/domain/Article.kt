package com.example.newsreaderapp.domain

data class Article(
    val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String,
    val url: String // Tambahkan field URL
)
