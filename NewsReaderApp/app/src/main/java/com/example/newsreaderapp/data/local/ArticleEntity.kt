package com.example.newsreaderapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val content: String,
    val imageUrl: String,
    val url: String // Tambahkan field URL
)
