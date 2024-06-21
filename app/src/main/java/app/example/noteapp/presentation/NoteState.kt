package app.example.noteapp.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import app.example.noteapp.data.NoteAndTags

data class NoteState(
    val notes: List<NoteAndTags> = emptyList(),
    val name: MutableState<String> = mutableStateOf(""),
    val ingredients: MutableState<String> = mutableStateOf(""),
    val method: MutableState<String> = mutableStateOf(""),
    val noteId: MutableState<Long> = mutableLongStateOf(0),
    val tagField: MutableState<String> = mutableStateOf(""),
    val tags: MutableList<String> = mutableListOf(),
    val imageUrl: MutableState<String> = mutableStateOf(""),
    val imagePrompt: MutableState<String> = mutableStateOf(""),
    )