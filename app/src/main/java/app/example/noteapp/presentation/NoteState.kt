package app.example.noteapp.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import app.example.noteapp.data.Note
import app.example.noteapp.data.Tag

data class NoteState(
    val notes: List<Note> = emptyList(),
    val title: MutableState<String> = mutableStateOf(""),
    val description: MutableState<String> = mutableStateOf(""),
    val noteId: MutableState<Int> = mutableIntStateOf(0),
    val tagField: MutableState<String> = mutableStateOf(""),
    val tags: MutableList<String> = mutableListOf(),
    )