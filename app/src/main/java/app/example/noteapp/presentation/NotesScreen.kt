package app.example.noteapp.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import app.example.noteapp.R
import app.example.noteapp.viewModel.NoteState
import app.example.noteapp.viewModel.NotesEvent
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    state: NoteState,
    navController: NavController,
    onEvent: (NotesEvent) -> Unit
) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(start = 16.dp, end = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.app_name),
                    modifier = Modifier.weight(1f),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                IconButton(
                    onClick = {
                        // set the title and description to empty string before navigate to add new note screen
                        state.name.value = ""
                        state.ingredients.value = ""
                        state.method.value = ""
                        state.tagField.value = ""
                        state.tags.clear()
                        state.imagePrompt.value = ""
                        state.imageUrl.value = ""
                        navController.navigate("AddNoteScreen")
                    },
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .size(40.dp)
                        .background(Color.White)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add new recipe"
                    )
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(state.notes.size) { index ->
                NoteItem(
                    state = state,
                    index = index,
                    navController = navController,
                    onEvent = onEvent
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteItem(
    state: NoteState,
    navController: NavController,
    index: Int,
    onEvent: (NotesEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(12.dp)
    ) {
        Row {
            Row( modifier = Modifier.weight(1f)){
                Surface(
                    color = Color(0xFFA1E2EB),
                    shape = RoundedCornerShape(10.dp),
                    onClick = {
                        state.name.value = state.notes[index].note.name
                        state.ingredients.value = state.notes[index].note.ingredients
                        state.noteId.value = state.notes[index].note.noteId
                        state.method.value = state.notes[index].note.method
                        state.imagePrompt.value = ""
                        state.imageUrl.value = state.notes[index].note.imageUrl
                        state.tags.clear()
                        state.notes[index].tags?.let {
                            state.tags.addAll(it.splitToSequence(','))
                        }

                        navController.navigate("IndividualNoteScreen")
                    }
                ) {
                    Text(
                        text = state.notes[index].note.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 5.dp
                        )
                    )
                }
            }
            // edit note
            IconButton(
                onClick = {
                    state.name.value = state.notes[index].note.name
                    state.ingredients.value = state.notes[index].note.ingredients
                    state.noteId.value = state.notes[index].note.noteId
                    state.method.value = state.notes[index].note.method
                    state.imagePrompt.value = ""
                    state.imageUrl.value = state.notes[index].note.imageUrl
                    state.tags.clear()
                    state.notes[index].tags?.let {
                        state.tags.addAll(it.splitToSequence(','))
                    }
                    navController.navigate("EditNoteScreen")
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Edit,
                    contentDescription = "Edit recipe",
                    modifier = Modifier.size(30.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            // delete note
            IconButton(
                onClick = {
                    onEvent(NotesEvent.DeleteNote(state.notes[index].note))
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = "Delete recipe",
                    modifier = Modifier.size(30.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Row() {
            // ingredients
            Text(
                text = state.notes[index].note.ingredients,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .weight(1f)
            )

            // image
            AsyncImage(
                model = state.notes[index].note.imageUrl,
                contentDescription = "Ai generated image",
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(8.dp)),
                alignment = Alignment.Center,
                contentScale = ContentScale.Crop,
            )
        }
    }
}