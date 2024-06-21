package app.example.noteapp.presentation

import app.example.noteapp.data.Note
import coil.request.Tags

sealed interface NotesEvent {
    // define different types of events that user can make
    data class DeleteNote(val note: Note): NotesEvent
    data class SaveNote(
        val noteId: Long,
        val name: String,
        val ingredients: String,
        val method: String,
        val imageUrl: String,
        val tags: MutableList<String>
    ): NotesEvent

    data class SaveNewNote(
        val name: String,
        val ingredients: String,
        val method: String,
        val imageUrl: String,
        val tags: MutableList<String>
    ): NotesEvent
}