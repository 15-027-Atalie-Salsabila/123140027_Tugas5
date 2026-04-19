package com.example.notesappnavigation.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notesappnavigation.database.NoteEntity
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun NoteListScreen(
    notes: List<NoteEntity>,
    isLoading: Boolean,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onNoteClick: (Long) -> Unit,
    onDeleteNote: (Long) -> Unit,
    onToggleFavorite: (Long, Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Your Collection",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                "You have ${notes.size} notes today",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (notes.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color.LightGray)
                    Text("Tidak Ada Catatan.", color = Color.Gray)
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(bottom = 80.dp, start = 16.dp, end = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(notes) { note ->
                    NoteItemEntity(
                        note = note,
                        onNoteClick = { onNoteClick(note.id) },
                        onDeleteClick = { onDeleteNote(note.id) },
                        onFavClick = { onToggleFavorite(note.id, note.isFavorite == 1L) }
                    )
                }
            }
        }
    }
}

@Composable
fun FavoritesScreen(
    favoriteNotes: List<NoteEntity>,
    onNoteClick: (Long) -> Unit,
    onToggleFavorite: (Long, Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Text(
            "Favorites",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp),
            color = Color.Black
        )
        
        if (favoriteNotes.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Favorite, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color.LightGray)
                    Text("Tidak Ada Favorite.", color = Color.Gray)
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(favoriteNotes) { note ->
                    NoteItemEntity(
                        note = note,
                        onNoteClick = { onNoteClick(note.id) },
                        onDeleteClick = null,
                        onFavClick = { onToggleFavorite(note.id, note.isFavorite == 1L) }
                    )
                }
            }
        }
    }
}

@Composable
fun NoteItemEntity(
    note: NoteEntity,
    onNoteClick: () -> Unit,
    onDeleteClick: (() -> Unit)?,
    onFavClick: () -> Unit
) {
    val isFav = note.isFavorite == 1L
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onNoteClick() }
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFE8EAF6)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Notifications, contentDescription = null, tint = Color(0xFF2196F3), modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(note.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.Black)
                Text(note.description, style = MaterialTheme.typography.bodySmall, color = Color.Gray, maxLines = 1)
            }
            IconButton(onClick = onFavClick) {
                Icon(
                    imageVector = if (isFav) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (isFav) Color.Red else Color.Gray
                )
            }
            if (onDeleteClick != null) {
                IconButton(onClick = onDeleteClick) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.LightGray)
                }
            }
        }
    }
}

@Composable
fun SettingsScreen(
    isDarkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    sortOrder: String,
    onSortOrderChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Dark Mode")
            Switch(checked = isDarkMode, onCheckedChange = onDarkModeChange)
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        Text("Sort Order", style = MaterialTheme.typography.titleMedium)
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = sortOrder == "newest", onClick = { onSortOrderChange("newest") })
            Text("Newest First", modifier = Modifier.clickable { onSortOrderChange("newest") })
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = sortOrder == "oldest", onClick = { onSortOrderChange("oldest") })
            Text("Oldest First", modifier = Modifier.clickable { onSortOrderChange("oldest") })
        }
    }
}

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F4F8)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF2196F3), Color(0xFF009688))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(50.dp),
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Atalie Salsabila",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            ProfileInfoCard(icon = Icons.Default.Info, label = "NIM", value = "123140027")
            Spacer(modifier = Modifier.height(12.dp))
            ProfileInfoCard(icon = Icons.Default.Face, label = "Rule", value = "Student / Android Developer")
            Spacer(modifier = Modifier.height(12.dp))
            ProfileInfoCard(icon = Icons.Default.LocationOn, label = "Location", value = "Lampung, Indonesia")
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { /* Logout action */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFDE7E7)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Logout", color = Color(0xFFD32F2F))
        }
    }
}

@Composable
fun ProfileInfoCard(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2196F3)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = label, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = Color.Black)
            }
        }
    }
}

@Composable
fun NoteDetailScreen(note: NoteEntity, onEditClick: (Long) -> Unit, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text("Note Detail", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            IconButton(onClick = { onEditClick(note.id) }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }
        }

        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2196F3)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Notifications,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color(0xFF009688)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Reminder: ${note.reminder}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF009688)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFFF0F0F0))
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = note.content,
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 24.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
fun AddNoteScreen(onSave: (String, String, String, String) -> Unit, onBack: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var reminder by remember { mutableStateOf("") }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val timePickerDialog = TimePickerDialog(
                context,
                { _, hourOfDay, minute ->
                    val selectedCalendar = Calendar.getInstance().apply {
                        set(year, month, dayOfMonth, hourOfDay, minute)
                    }
                    val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
                    reminder = formatter.format(selectedCalendar.time)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
            )
            timePickerDialog.show()
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Tambah Catatan", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Judul") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Deskripsi") }, modifier = Modifier.fillMaxWidth())
        
        Box(modifier = Modifier.fillMaxWidth().clickable { datePickerDialog.show() }) {
            OutlinedTextField(
                value = reminder,
                onValueChange = { },
                label = { Text("Reminder (Klik untuk pilih waktu)") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
        
        OutlinedTextField(value = content, onValueChange = { content = it }, label = { Text("Isi") }, modifier = Modifier.fillMaxWidth().weight(1f))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = onBack, modifier = Modifier.weight(1f)) { Text("Batal") }
            Button(onClick = { onSave(title, description, content, reminder) }, modifier = Modifier.weight(1f)) { Text("Simpan") }
        }
    }
}

@Composable
fun EditNoteScreen(note: NoteEntity, onSave: (Long, String, String, String, String) -> Unit, onBack: () -> Unit) {
    var title by remember { mutableStateOf(note.title) }
    var description by remember { mutableStateOf(note.description) }
    var content by remember { mutableStateOf(note.content) }
    var reminder by remember { mutableStateOf(note.reminder) }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val timePickerDialog = TimePickerDialog(
                context,
                { _, hourOfDay, minute ->
                    val selectedCalendar = Calendar.getInstance().apply {
                        set(year, month, dayOfMonth, hourOfDay, minute)
                    }
                    val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
                    reminder = formatter.format(selectedCalendar.time)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
            )
            timePickerDialog.show()
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Edit Catatan", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Judul") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Deskripsi") }, modifier = Modifier.fillMaxWidth())
        
        Box(modifier = Modifier.fillMaxWidth().clickable { datePickerDialog.show() }) {
            OutlinedTextField(
                value = reminder,
                onValueChange = { },
                label = { Text("Reminder (Klik untuk pilih waktu)") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }

        OutlinedTextField(value = content, onValueChange = { content = it }, label = { Text("Isi") }, modifier = Modifier.fillMaxWidth().weight(1f))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = onBack, modifier = Modifier.weight(1f)) { Text("Batal") }
            Button(onClick = { onSave(note.id, title, description, content, reminder) }, modifier = Modifier.weight(1f)) { Text("Simpan") }
        }
    }
}
