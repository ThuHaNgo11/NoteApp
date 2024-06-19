package app.example.noteapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Note::class, Group::class, Tag::class, NoteTagCrossRef::class],
    version = 1
)

abstract class NotesDb : RoomDatabase() {
    abstract val dao: NoteDao
}