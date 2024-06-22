package app.example.noteapp.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.Brush
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import app.example.noteapp.R
import app.example.noteapp.repository.ImageRepository
import coil.compose.AsyncImage
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddNoteScreen(
    state: NoteState,
    navController: NavController,
    imageRepository: ImageRepository,
    onEvent: (NotesEvent) -> Unit
) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .background(MaterialTheme.colorScheme.primary),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    // TODO: update state of field to empty
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBackIos,
                        contentDescription = "Back",
                        modifier = Modifier
                            .size(25.dp),
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
                Text(
                    text = "New Recipe",
                    modifier = Modifier.weight(1f),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(
                    NotesEvent.SaveNewNote(
                        name = state.name.value,
                        ingredients = state.name.value,
                        method = state.method.value,
                        imageUrl = state.imageUrl.value,
                        tags = state.tags
                    )
                )
                // go back to notes screen
                navController.popBackStack();
            }) {

                Icon(imageVector = Icons.Rounded.Check, contentDescription = "Save note")
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // recipe name
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                value = state.name.value,
                onValueChange = {
                    state.name.value = it // when changes are made, it is assigned here
                },
                textStyle = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 17.sp
                ),
                placeholder = {
                    Text(text = "Name")
                }
            )

            // ingredients
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                value = state.ingredients.value,
                onValueChange = {
                    state.ingredients.value = it // when changes are made, it is assigned here
                },
                placeholder = {
                    Text(text = "Ingredients")
                }
            )

            // method
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                value = state.method.value,
                onValueChange = {
                    state.method.value = it // when changes are made, it is assigned here
                },
                placeholder = {
                    Text(text = "Method")
                }
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Add tag
                TextField(
                    modifier = Modifier
                        .padding(16.dp),
                    value = state.tagField.value,
                    onValueChange = {
                        state.tagField.value = it
                    },
                    placeholder = {
                        Text(text = "Tag")
                    }
                )

                // confirm tag
                IconButton(
                    onClick = {
                        if (state.tagField.value !== "") {
                            if (state.tags.indexOf(state.tagField.value) < 0) {
                                state.tags.add(state.tagField.value)
                            }
                            state.tagField.value = ""
                        }
                    },
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = "Add tag",
                        modifier = Modifier
                            .size(30.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                repeat(state.tags.size) {
                    Box(
                        Modifier
                            .border(1.dp, Color.DarkGray, RoundedCornerShape(10.dp))
                            .padding(start = 8.dp)
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = state.tags[it],
                                fontSize = 18.sp
                            )
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(
                                    imageVector = Icons.Rounded.Cancel,
                                    contentDescription = "Delete tag",
                                    modifier = Modifier
                                        .size(30.dp),
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                }
            }

            // openai image generation
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Prompt to generate image
                TextField(
                    modifier = Modifier
                        .padding(16.dp),
                    value = state.imagePrompt.value,
                    onValueChange = {
                        state.imagePrompt.value = it
                    },
                    placeholder = {
                        Text(text = "Prompt to generate image")
                    }
                )

                // Generate image button
                IconButton(
                    onClick = {
                        if (state.imagePrompt.value !== "") {
                            // call Dalle2 api
                            runBlocking {
                                val job =
                                    async { imageRepository.makeImageGenerationRequest(state.imagePrompt.value) }
                                val result = job.await()
                                state.imageUrl.value = result
                            }
                        }
                    },
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Brush,
                        contentDescription = "Generate image",
                        modifier = Modifier
                            .size(30.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Box(modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 10.dp)) {
                if (state.imageUrl.value !== "") {
                    AsyncImage(
                        model = state.imageUrl.value,
                        contentDescription = "Ai generated image",
                        modifier = Modifier
                            .size(380.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .align(Alignment.Center),
                        contentScale = ContentScale.Crop,
                    )
                }
            }
        }
    }
}

