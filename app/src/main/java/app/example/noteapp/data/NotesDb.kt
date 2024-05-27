package app.example.noteapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

// The class is annotated with a @Database annotation
// that includes an entities array that lists
// all of the data entities associated with the database.
@Database(
    entities = [Note::class],
    version = 1
)
// an abstract class that extends RoomDatabase
abstract class NotesDb : RoomDatabase() {
    // For each DAO class that is associated with the database,
    // the database class must define an abstract method
    // that has zero arguments and returns an instance of the DAO class.
    abstract val dao: NoteDao
}