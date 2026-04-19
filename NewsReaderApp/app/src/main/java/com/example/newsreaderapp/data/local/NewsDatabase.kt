package com.example.newsreaderapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ArticleEntity::class], version = 2, exportSchema = false)
abstract class NewsDatabase : RoomDatabase() {
    abstract val dao: ArticleDao
}
