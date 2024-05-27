package app.example.noteapp.presentation

import app.example.noteapp.data.Note

sealed interface NotesEvent {
    // define different types of events that user can make
    //object SortNotes: NotesEvent
    data class DeleteNote(val note: Note): NotesEvent
    data class SaveNote(
        val title: String,
        val description: String
    ): NotesEvent
}