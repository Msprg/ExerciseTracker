package com.msprg.exerciseTracker.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.EventRepeat
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.msprg.exerciseTracker.ExTrApplication
import com.msprg.exerciseTracker.MainActivityViewModel
import com.msprg.exerciseTracker.data.ExerciseIcon
import com.msprg.exerciseTracker.data.ExercisesList
import com.msprg.exerciseTracker.data.RoutineItem
import com.msprg.exerciseTracker.data.RoutinesList
import com.msprg.exerciseTracker.ui.components.RowItem
import com.msprg.exerciseTracker.ui.navigation.AppNavCtl
import com.msprg.exerciseTracker.ui.navigation.Screens
import com.msprg.exerciseTracker.ui.theme.ExerciseTrackerTheme
import com.msprg.exerciseTracker.ui.viewmodels.RoutinesViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import java.util.UUID

@Composable
fun RoutinesScreen(
    navCtl: NavController,
    viewModel: RoutinesViewModel = RoutinesViewModel(ExTrApplication.datastoremodule)
) {
    val routinesData by viewModel.routinesDataFlow.collectAsState(initial = RoutinesList())

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navCtl.navigate(Screens.RoutineItemEditScreen.name)
                },
                shape = CircleShape
            ) {
                Icon(Icons.Rounded.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            items(routinesData.routineList, key = { it.id }) { routineItem ->
                RowItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.EventRepeat,
                            contentDescription = null,
                            modifier = Modifier
                                .size(55.dp)
                                .padding(start = 8.dp)
                        )
                    },
                    title = routineItem.routineTitle,
                    description = "${routineItem.id} ${routineItem.routineDescription}",
                    onClick = {
//                        navCtl.navigate("${Screens.RoutineItemViewScreen.name}/${routineItem.id}")
                        navCtl.navigate("${Screens.RoutineItemEditScreen.name}/${routineItem.id}")
                    },
                    onDismissToStart = {
                        viewModel.deleteRoutine(routineItem.id)
                    },
                    trailingContent = {
                        IconButton(
                            onClick = {
                                // Handle play button click
                            },
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.PlayArrow,
                                tint = Color.Green,
                                contentDescription = "Play",
                                modifier = Modifier.size(55.dp)
                            )
                        }
                    }
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineItemEditScreen(
    routineItem: RoutineItem?,
    onBackPressed: () -> Unit,
    onSavePressed: (RoutineItem) -> Unit,
    viewModel: MainActivityViewModel = MainActivityViewModel(ExTrApplication.datastoremodule)

) {
    val exerciseData by viewModel.exerciseDataFlow.collectAsState(initial = ExercisesList())

    var editedTitle by remember { mutableStateOf(routineItem?.routineTitle ?: "") }
    var editedDescription by remember { mutableStateOf(routineItem?.routineDescription ?: "") }
    var editedExerciseList by remember {
        mutableStateOf(
            routineItem?.exerciseList ?: persistentListOf()
        )
    }

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    var textFieldValue by remember { mutableStateOf(TextFieldValue(editedTitle)) }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        textFieldValue = textFieldValue.copy(selection = TextRange(editedTitle.length))
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val updatedRoutineItem = RoutineItem(
                        id = routineItem?.id ?: UUID.randomUUID().toString(),
                        routineTitle = editedTitle,
                        routineDescription = editedDescription,
                        exerciseList = editedExerciseList,
                        repetitions = routineItem?.repetitions ?: 1
                    )
                    onSavePressed(updatedRoutineItem)
                },
                shape = CircleShape
            ) {
                Icon(Icons.Rounded.Check, contentDescription = "Save")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
//                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = textFieldValue,
                onValueChange = {
                    textFieldValue = it
                    editedTitle = it.text
                },
                label = { Text("Title") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }),
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            OutlinedTextField(
                value = editedDescription,
                onValueChange = { editedDescription = it },
                label = { Text("Description") },
                minLines = 4,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            var repetitionsText by remember {
                mutableStateOf(
                    routineItem?.repetitions?.toString() ?: ""
                )
            }

            OutlinedTextField(
                value = repetitionsText,
                onValueChange = { newValue ->
                    repetitionsText = newValue.filter { it.isDigit() }
                },
                label = { Text("Repetitions") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier
                    .align(Alignment.End)
//                    .fillMaxWidth()
                    .padding(8.dp)
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                items(editedExerciseList, key = { it.exerciseId }) { routineExercise ->
                    val exercise = exerciseData.excList.find { it.id == routineExercise.exerciseId }
                    RowItem(
                        icon = {
                            when (val icon = exercise?.icon) {
                                is ExerciseIcon.DefaultIcon -> DefaultVectorIcon(
                                    modifier = Modifier
                                        .size(55.dp)
                                        .padding(start = 8.dp)
                                )

                                is ExerciseIcon.RasterIcon -> RasterIcon(
                                    modifier = Modifier
                                        .size(55.dp)
                                        .padding(start = 8.dp),
                                    base64String = icon.imageBase64
                                )

                                else -> Box(modifier = Modifier.size(55.dp))
                            }
                        },
                        title = exercise?.exTitle ?: "",
                        description = "Duration: ${routineExercise.duration}, Repetitions: ${routineExercise.repetitions}",
                        onClick = {
                            // Handle exercise click if needed
                        },
                        onDismissToStart = {
                            // Remove the exercise from the editedExerciseList
                            editedExerciseList = editedExerciseList.filterNot {
                                it.exerciseId == routineExercise.exerciseId
                            }.toPersistentList()
                        }
                    )
                }
            }
            Spacer(Modifier.weight(1f))

            Button(modifier = Modifier
                .align(Alignment.CenterHorizontally), onClick = {

                //todo: popup that lists all the exercises, and lets user to select one, adding it to editedExerciseList
            }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Exercise",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Exercise")

            }
        }
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun RoutinesScreenPrew() {
    ExerciseTrackerTheme {
        AppNavCtl(Screens.RoutinesScreen)
    }
}
