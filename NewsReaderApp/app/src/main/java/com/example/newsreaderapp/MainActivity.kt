package com.example.newsreaderapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import com.example.newsreaderapp.data.local.NewsDatabase
import com.example.newsreaderapp.data.repository.NewsRepositoryImpl
import com.example.newsreaderapp.presentation.ArticleDetailScreen
import com.example.newsreaderapp.presentation.NewsScreen
import com.example.newsreaderapp.presentation.NewsViewModel
import com.example.newsreaderapp.ui.theme.NewsReaderAppTheme
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Manual Dependency Injection
        val db = Room.databaseBuilder(
            applicationContext,
            NewsDatabase::class.java,
            "news_db"
        )
        .fallbackToDestructiveMigration() // Tambahkan ini biar nggak force close pas ganti struktur DB
        .build()

        val client = HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
            install(Logging) {
                level = LogLevel.INFO
            }
        }

        val repository = NewsRepositoryImpl(client, db.dao)

        enableEdgeToEdge()
        setContent {
            NewsReaderAppTheme {
                val navController = rememberNavController()
                
                NavHost(navController = navController, startDestination = "news_list") {
                    composable("news_list") {
                        val viewModel: NewsViewModel = viewModel(
                            factory = object : ViewModelProvider.Factory {
                                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                    @Suppress("UNCHECKED_CAST")
                                    return NewsViewModel(repository) as T
                                }
                            }
                        )
                        NewsScreen(
                            state = viewModel.state.value,
                            onRefresh = { viewModel.getArticles() },
                            onArticleClick = { article ->
                                navController.navigate("article_detail/${article.id}")
                            }
                        )
                    }
                    composable(
                        route = "article_detail/{articleId}",
                        arguments = listOf(navArgument("articleId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val articleId = backStackEntry.arguments?.getInt("articleId") ?: -1
                        ArticleDetailScreen(
                            articleId = articleId,
                            repository = repository,
                            onBackClick = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
