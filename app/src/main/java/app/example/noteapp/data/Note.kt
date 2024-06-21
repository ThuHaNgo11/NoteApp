package app.example.noteapp.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class Note(
    val name: String,
    val ingredients: String,
    val method: String,
    val imageUrl: String,
    val dateAdded: Long,

    @PrimaryKey(autoGenerate = true)
    val noteId: Long = 0,

    // foreign key
    val refGroupId: Long? = null
)

@Entity(indices = [Index(value=["name"], unique = true)])
data class Tag(
    val name: String,

    @PrimaryKey(autoGenerate = true)
    val tagId: Long = 0
)

@Entity
data class Group(
    val name: String,

    @PrimaryKey(autoGenerate = true)
    val groupId: Long = 0
)

@Entity(primaryKeys = ["noteId", "tagId"])
data class NoteTagCrossRef(
    val noteId: Long,
    val tagId: Long
)

data class GroupWithNotes(
    @Embedded val group: Group,
    @Relation(
        parentColumn = "groupId",
        entityColumn = "refGroupId"
    )
    val notes: List<Note>
)

data class NoteAndTags (
    @Embedded val note: Note,
    val tags: String? = null
)