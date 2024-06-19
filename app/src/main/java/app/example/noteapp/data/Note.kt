package app.example.noteapp.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class Note(
    val title: String,
    val description: String,
    val dateAdded: Long,

    @PrimaryKey(autoGenerate = true)
    val noteId: Int = 0,

    // foreign key
    val refGroupId: Int? = null
)

@Entity
data class Tag(
    val name: String,
    @PrimaryKey(autoGenerate = true)
    val tagId: Int = 0
)

@Entity
data class Group(
    val name: String,

    @PrimaryKey(autoGenerate = true)
    val groupId: Int = 0
)

@Entity(primaryKeys = ["noteId", "tagId"])
data class NoteTagCrossRef(
    val noteId: Int,
    val tagId: Int
)

data class GroupWithNotes(
    @Embedded val group: Group,
    @Relation(
        parentColumn = "groupId",
        entityColumn = "refGroupId"
    )
    val notes: List<Note>
)