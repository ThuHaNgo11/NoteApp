package app.example.noteapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    // insert and update
    @Upsert
    suspend fun upsertNote(note: Note)
    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM note ORDER BY dateAdded DESC")
    fun getNoteOrderByDateAdded(): Flow<List<Note>>

    @Transaction
    @Query("SELECT * FROM `group`")
    fun getGroupsWithNotes(): List<GroupWithNotes>
}
