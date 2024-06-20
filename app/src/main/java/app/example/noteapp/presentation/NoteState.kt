package app.example.noteapp.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import app.example.noteapp.data.Note

data class NoteState(
    val notes: List<Note> = emptyList(),
    val name: MutableState<String> = mutableStateOf(""),
    val ingredients: MutableState<String> = mutableStateOf(""),
    val method: MutableState<String> = mutableStateOf(""),
    val noteId: MutableState<Int> = mutableIntStateOf(0),
    val tagField: MutableState<String> = mutableStateOf(""),
    val tags: MutableList<String> = mutableListOf(),
    val imageUrl: MutableState<String> = mutableStateOf(""),
    val imagePrompt: MutableState<String> = mutableStateOf(""),
    )