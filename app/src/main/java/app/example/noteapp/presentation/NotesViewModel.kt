package app.example.noteapp.presentation

import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.example.noteapp.data.Note
import app.example.noteapp.data.NoteDao
import app.example.noteapp.data.NoteTagCrossRef
import app.example.noteapp.data.Tag
import app.example.noteapp.repository.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotesViewModel(
    private val dao: NoteDao //pass param to the constructor
) : ViewModel() {
    private val imageRepository = ImageRepository()
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
                viewModelScope.launch {
                    dao.deleteNote(event.note)
                }
            }

            is NotesEvent.SaveNewNote -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val note = Note(
                        name = event.name,
                        ingredients = event.ingredients,
                        method = event.method,
                        imageUrl = event.imageUrl,
                        dateAdded = System.currentTimeMillis()
                    )
                    val noteId = dao.upsertNote(note)
                    val tags = event.tags.map{ Tag(name = it) }
                    dao.insertTags(tags)
                    val tagsFromDb = dao.getTagsByName(event.tags)
                    dao.insertNoteTagCrossRef(tagsFromDb.map{ NoteTagCrossRef(noteId, it) })
                }

                _state.update{
                    it.copy(
                        name = mutableStateOf(""),
                        ingredients = mutableStateOf(""),
                        method = mutableStateOf(""),
                        imageUrl = mutableStateOf(""),
                        imagePrompt = mutableStateOf(""),
                        tagField = mutableStateOf(""),
                        tags = mutableListOf()
                    )
                }
            }

            is NotesEvent.SaveNote -> {
                viewModelScope.launch(Dispatchers.IO) { // bc the dao is async
                    val note = Note(
                        noteId = event.noteId,
                        name = event.name,
                        ingredients = event.ingredients,
                        method = event.method,
                        imageUrl = event.imageUrl,
                        dateAdded = System.currentTimeMillis()
                    )
                    dao.upsertNote(note)
                    val tags = event.tags.map{ Tag(name = it) }
                    dao.insertTags(tags)
                    val tagsFromDb = dao.getTagsByName(event.tags)
                    dao.deleteAllTagsForNote(event.noteId)
                    dao.insertNoteTagCrossRef(tagsFromDb.map{ NoteTagCrossRef(event.noteId, it) })
                }

                _state.update{
                    it.copy(
                        noteId = mutableLongStateOf(0),
                        name = mutableStateOf(""),
                        ingredients = mutableStateOf(""),
                        method = mutableStateOf(""),
                        imageUrl = mutableStateOf(""),
                        imagePrompt = mutableStateOf(""),
                        tagField = mutableStateOf(""),
                        tags = mutableListOf()
                    )
                }
            }

            is NotesEvent.GenerateImage -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val result = imageRepository.makeImageGenerationRequest(event.prompt)
                    event.setAnimState(false)
                    _state.update{
                        it.copy(
                            imageUrl = mutableStateOf(result)
                        )
                    }
                }
            }
        }
    }
}