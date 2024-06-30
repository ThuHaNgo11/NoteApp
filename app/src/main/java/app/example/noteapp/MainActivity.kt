package app.example.noteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import app.example.noteapp.data.NotesDb
import app.example.noteapp.presentation.AddNoteScreen
import app.example.noteapp.presentation.EditNoteScreen
import app.example.noteapp.presentation.IndividualNoteScreen
import app.example.noteapp.presentation.NotesScreen
import app.example.noteapp.viewModel.NotesViewModel
import app.example.noteapp.repository.ImageRepository
import app.example.noteapp.ui.theme.NoteAppTheme

class MainActivity : ComponentActivity() {

    // manually inject dependencies
    private val database: NotesDb by lazy {
        Room.databaseBuilder(
            applicationContext,
            NotesDb::class.java,
            "notes.db"
        ).build()
    }

    private val viewModel by viewModels<NotesViewModel> (
        // create factory
        factoryProducer = {
            object: ViewModelProvider.Factory {
                override fun<T: ViewModel> create(modelClass: Class<T>): T{
                    return NotesViewModel(database.dao) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val state by viewModel.state.collectAsState()
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "NotesScreen") {
                        composable("NotesScreen"){
                            NotesScreen(
                                state = state,
                                navController = navController,
                                onEvent = viewModel::onEvent
                            )
                        }

                        composable("AddNoteScreen"){
                            AddNoteScreen(
                                state = state,
                                navController = navController,
                                onEvent = viewModel::onEvent
                            )
                        }

                        composable("EditNoteScreen"){
                            EditNoteScreen(
                                state = state,
                                navController = navController,
                                onEvent = viewModel::onEvent
                                )
                        }

                        composable("IndividualNoteScreen"){
                            IndividualNoteScreen(
                                state = state,
                                navController = navController,
                                onEvent = viewModel::onEvent
                            )
                        }
                    }
                }
            }
        }
    }
}
