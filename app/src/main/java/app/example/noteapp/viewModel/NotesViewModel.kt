package app.example.noteapp.viewModel

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
    // Private properties
    private val imageRepository = ImageRepository()
    private var notes = dao.getNoteOrderByDateAdded()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    // State Flow properties
    val _state = MutableStateFlow(NoteState())
    val state = combine(_state, notes) { state, notes ->
        state.copy(
            notes = notes
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteState())

    // Event handling function
    fun onEvent(event: NotesEvent) {
        when (event) {
            // Handling Delete recipe event
            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    dao.deleteNote(event.note)
                }
            }

            // Handling Save new/add recipe event
            is NotesEvent.SaveNewNote -> {
                viewModelScope.launch(Dispatchers.IO) {
                    // Create a new note object
                    val note = Note(
                        name = event.name,
                        ingredients = event.ingredients,
                        method = event.method,
                        imageUrl = event.imageUrl,
                        dateAdded = System.currentTimeMillis()
                    )
                    // Upsert note in the database and get its ID
                    val noteId = dao.upsertNote(note)

                    // Create tags and insert them into the database
                    val tags = event.tags.map{ Tag(name = it) }
                    dao.insertTags(tags)

                    // Retrieve tags from the database by name
                    val tagsFromDb = dao.getTagsByName(event.tags)

                    // Insert the note-tag relationships into the database
                    dao.insertNoteTagCrossRef(tagsFromDb.map{ NoteTagCrossRef(noteId, it) })
                }

                // Reset the state to empty values
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

            // Handling Save edited recipe event
            is NotesEvent.SaveNote -> {
                viewModelScope.launch(Dispatchers.IO) { // bc the dao is async
                    // Create a note object with updated values
                    val note = Note(
                        noteId = event.noteId,
                        name = event.name,
                        ingredients = event.ingredients,
                        method = event.method,
                        imageUrl = event.imageUrl,
                        dateAdded = System.currentTimeMillis()
                    )

                    // Upsert the updated note in the database
                    dao.upsertNote(note)

                    // Create tags and insert them into the database
                    val tags = event.tags.map{ Tag(name = it) }
                    dao.insertTags(tags)

                    // Retrieve tags from the database by name
                    val tagsFromDb = dao.getTagsByName(event.tags)

                    // Delete existing note-tag relationships for the note
                    dao.deleteAllTagsForNote(event.noteId)

                    // Insert the updated note-tag relationships into the database
                    dao.insertNoteTagCrossRef(tagsFromDb.map{ NoteTagCrossRef(event.noteId, it) })
                }

                // Reset the state to empty values
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

            // Handling GenerateImage event
            is NotesEvent.GenerateImage -> {
                viewModelScope.launch(Dispatchers.IO) {
                    // Make image generation request using the image repository
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