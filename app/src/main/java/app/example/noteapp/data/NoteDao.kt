package app.example.noteapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    // insert and update
    @Upsert
    suspend fun upsertNote(note: Note): Long
    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT note.*, GROUP_CONCAT(tag.name) as tags FROM note " +
            "LEFT JOIN notetagcrossref ON note.noteId = notetagcrossref.noteId " +
            "LEFT JOIN tag ON tag.tagId = notetagcrossref.tagId " +
            "GROUP BY note.noteId " +
            "ORDER BY dateAdded DESC")
    fun getNoteOrderByDateAdded(): Flow<List<NoteAndTags>>

    @Transaction
    @Query("SELECT * FROM `group`")
    fun getGroupsWithNotes(): List<GroupWithNotes>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTags(tags: List<Tag>): List<Long>

    @Query("SELECT tagId FROM tag WHERE name IN (:names)")
    fun getTagsByName(names: List<String>): List<Long>

    @Delete
    suspend fun deleteNoteTagCrossRef(noteTagRef: NoteTagCrossRef)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertNoteTagCrossRef(noteTagRefs: List<NoteTagCrossRef>)

    @Query("DELETE FROM NoteTagCrossRef WHERE noteId = :noteId")
    suspend fun deleteAllTagsForNote(noteId: Long)
}
