package com.example.notesappnavigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.notesappnavigation.components.MyBottomBar
import com.example.notesappnavigation.navigation.*
import com.example.notesappnavigation.screens.*
import com.example.notesappnavigation.database.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val noteViewModel: NoteViewModel = viewModel()
    val settingsDataStore = remember { SettingsDataStore(context) }
    
    val notes by noteViewModel.notes.collectAsState()
    val favoriteNotes by noteViewModel.favoriteNotes.collectAsState()
    val isLoading by noteViewModel.isLoading.collectAsState()
    val searchQuery by noteViewModel.searchQuery.collectAsState()
    val isDarkMode by settingsDataStore.isDarkMode.collectAsState(initial = false)
    val sortOrder by settingsDataStore.sortOrder.collectAsState(initial = "newest")
    val scope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "My Notes",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF2196F3)
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = { MyBottomBar(navController) },
        floatingActionButton = {
            if (currentRoute == BottomNavItem.Notes.route) {
                ExtendedFloatingActionButton(
                    onClick = { navController.navigate(Screen.AddNote.route) },
                    containerColor = Color(0xFF2196F3),
                    contentColor = Color.White,
                    icon = { Icon(Icons.Default.Add, contentDescription = "Add") },
                    text = { Text("New Note") }
                )
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Notes.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(BottomNavItem.Notes.route) {
                NoteListScreen(
                    notes = if (sortOrder == "newest") notes else notes.reversed(),
                    isLoading = isLoading,
                    searchQuery = searchQuery,
                    onSearchQueryChange = { noteViewModel.updateSearchQuery(it) },
                    onNoteClick = { id -> navController.navigate(Screen.NoteDetail.createRoute(id.toInt())) },
                    onDeleteNote = { id -> noteViewModel.deleteNote(id) },
                    onToggleFavorite = { id, isFav -> noteViewModel.toggleFavorite(id, isFav) }
                )
            }
            
            composable(BottomNavItem.Favorites.route) { 
                FavoritesScreen(
                    favoriteNotes = favoriteNotes,
                    onNoteClick = { id -> navController.navigate(Screen.NoteDetail.createRoute(id.toInt())) },
                    onToggleFavorite = { id, isFav -> noteViewModel.toggleFavorite(id, isFav) }
                )
            }
            
            composable(BottomNavItem.Profile.route) { ProfileScreen() }

            composable(BottomNavItem.Settings.route) {
                SettingsScreen(
                    isDarkMode = isDarkMode,
                    onDarkModeChange = { enabled ->
                        scope.launch { settingsDataStore.setDarkMode(enabled) }
                    },
                    sortOrder = sortOrder,
                    onSortOrderChange = { order ->
                        scope.launch { settingsDataStore.setSortOrder(order) }
                    }
                )
            }

            composable(Screen.AddNote.route) {
                AddNoteScreen(
                    onSave = { title, desc, content, reminder ->
                        noteViewModel.insertNote(title, desc, content, reminder)
                        navController.popBackStack()
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(
                Screen.NoteDetail.route,
                arguments = listOf(navArgument("noteId") { type = NavType.IntType })
            ) { entry ->
                val id = entry.arguments?.getInt("noteId")?.toLong() ?: 0L
                val note = notes.find { it.id == id }
                if (note != null) {
                    NoteDetailScreen(
                        note = note, 
                        onEditClick = { noteId -> navController.navigate(Screen.EditNote.createRoute(noteId.toInt())) },
                        onBack = { navController.popBackStack() }
                    )
                }
            }

            composable(
                Screen.EditNote.route,
                arguments = listOf(navArgument("noteId") { type = NavType.IntType })
            ) { entry ->
                val id = entry.arguments?.getInt("noteId")?.toLong() ?: 0L
                val note = notes.find { it.id == id }
                if (note != null) {
                    EditNoteScreen(
                        note = note,
                        onSave = { noteId, title, desc, content, reminder ->
                            noteViewModel.updateNote(noteId, title, desc, content, reminder)
                            navController.popBackStack()
                        },
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
