package app.example.noteapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

// data access object - allow interactions with stored data
// At compile time, Room automatically generates
// implementations of the DAOs that you define.
@Dao
interface NoteDao {
    // insert and update
    @Upsert
    suspend fun upsertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM note ORDER BY dateAdded")
    fun getNoteOrderByDateAdded(): Flow<List<Note>>
}
