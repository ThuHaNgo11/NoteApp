package app.example.noteapp.presentation

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.example.noteapp.data.Note
import app.example.noteapp.data.NoteDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotesViewModel(
    private val dao: NoteDao //pass param to the constructor
) : ViewModel() {
    private var notes = dao.getNoteOrderByDateAdded()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    val _state = MutableStateFlow(NoteState())
    val state = combine(_state, notes){ state, notes ->
        state.copy(
            notes = notes
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteState())
    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.DeleteNote -> {
                viewModelScope.launch { // bc the dao is async
                    dao.deleteNote(event.note)
                }
            }

            is NotesEvent.SaveNewNote -> {
                val note = Note(
                    title = state.value.title.value,
                    description = state.value.description.value,
                    dateAdded = System.currentTimeMillis()
                )

                viewModelScope.launch {
                    dao.upsertNote(note)
                }

                _state.update{
                    it.copy(
                        title = mutableStateOf(""),
                        description = mutableStateOf("")
                    )
                }
            }

            is NotesEvent.SaveNote -> {
                val note = Note(
                    noteId = state.value.noteId.value,
                    title = state.value.title.value,
                    description = state.value.description.value,
                    dateAdded = System.currentTimeMillis()
                )
                viewModelScope.launch { // bc the dao is async
                    dao.upsertNote(note)
                }
                _state.update{
                    it.copy(
                        noteId = mutableIntStateOf(0),
                        title = mutableStateOf(""),
                        description = mutableStateOf("")
                    )
                }
            }
        }
    }
}