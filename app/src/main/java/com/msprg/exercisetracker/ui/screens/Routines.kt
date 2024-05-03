package com.msprg.exerciseTracker.ui.screens

import android.content.res.Configuration
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.EventRepeat
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.msprg.exerciseTracker.ExTrApplication
import com.msprg.exerciseTracker.MainActivityViewModel
import com.msprg.exerciseTracker.data.ExerciseIcon
import com.msprg.exerciseTracker.data.ExerciseItem
import com.msprg.exerciseTracker.data.ExercisesList
import com.msprg.exerciseTracker.data.RoutineExercise
import com.msprg.exerciseTracker.data.RoutineItem
import com.msprg.exerciseTracker.data.RoutinesList
import com.msprg.exerciseTracker.ui.components.RowItem
import com.msprg.exerciseTracker.ui.navigation.AppNavCtl
import com.msprg.exerciseTracker.ui.navigation.Screens
import com.msprg.exerciseTracker.ui.theme.ExerciseTrackerTheme
import com.msprg.exerciseTracker.ui.viewmodels.HistoryViewModel
import com.msprg.exerciseTracker.ui.viewmodels.RoutinesViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.UUID


@Composable
fun RoutinesScreen(
    navCtl: NavController,
    viewModel: RoutinesViewModel = RoutinesViewModel(ExTrApplication.datastoremodule),
    mainViewModel: MainActivityViewModel = MainActivityViewModel(ExTrApplication.datastoremodule)
) {
    val routinesData by viewModel.routinesDataFlow.collectAsState(initial = RoutinesList())
    val exerciseData by mainViewModel.exerciseDataFlow.collectAsState(initial = ExercisesList())

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
                        navCtl.navigate("${Screens.RoutineItemEditScreen.name}/${routineItem.id}")
                    },
                    onDismissToStart = {
                        viewModel.deleteRoutine(routineItem.id)
                    },
                    trailingContent = {
                        IconButton(
                            onClick = {
                                val verifiedExerciseList = VerifyDataLinkageIntegrity(
                                    listOf(routineItem),
                                    exerciseData.excList
                                ).firstOrNull()?.exerciseList
                                if (verifiedExerciseList != null && verifiedExerciseList.isNotEmpty()) {
                                    navCtl.navigate("${Screens.PlayRoutineScreen.name}/${routineItem.id}")
                                } else {
                                    // Display a toast with an error message
                                    Toast.makeText(
                                        ExTrApplication.appContext,
                                        "Cannot play empty routine",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Toast.makeText(
                                        ExTrApplication.appContext,
                                        "Tap on the routine to add some exercises",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
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

@Composable
fun PlayRoutineScreen(
    routineItem: RoutineItem,
    exerciseList: List<RoutineExercise>,
    onRoutineFinished: () -> Unit,
    viewModel: MainActivityViewModel = MainActivityViewModel(ExTrApplication.datastoremodule),
    historyViewModel: HistoryViewModel = HistoryViewModel(ExTrApplication.datastoremodule)
) {
    val exerciseData by viewModel.exerciseDataFlow.collectAsState(initial = ExercisesList())
    var currentExerciseIndex by remember { mutableStateOf(0) }
    val currentExercise = exerciseList.getOrNull(currentExerciseIndex)
    val exercise =
        currentExercise?.let { exerciseData.excList.find { it.id == currentExercise.exerciseId } }

    var progress by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        var historyItemId: String? = null

        while (currentExerciseIndex < exerciseList.size) {
            val currentExercise = exerciseList.getOrNull(currentExerciseIndex)
            val exercise =
                currentExercise?.let { exerciseData.excList.find { it.id == currentExercise.exerciseId } }

            if (exercise != null) {
                if (historyItemId == null) {
                    // Add a new history item with unfinished state
                    historyItemId = historyViewModel.addHistoryItem(
                        routineId = routineItem.id,
                        routineTitle = routineItem.routineTitle,
                        startTime = System.currentTimeMillis(),
                        endTime = 0L,
                        completed = false
                    )
                }
                val exerciseDuration =
                    exercise.durationSeconds * 1000L * currentExercise.repetitions
                Log.d("170", "Waiting for $exerciseDuration")

                val startTime = System.currentTimeMillis()
                while (System.currentTimeMillis() - startTime < exerciseDuration) {
                    val elapsedTime = System.currentTimeMillis() - startTime
                    progress = elapsedTime.toFloat() / exerciseDuration
                    delay(16) // Update progress every 16ms (approximately 60 frames per second)
                }
                progress = 1f // Ensure progress reaches 100% at the end of the exercise
            } else {
                if (currentExercise != null) {
                    Log.d("182", "Exercise ${currentExercise.exerciseId} not found. Skipping...")
                }
            }

            currentExerciseIndex++
        }

        historyItemId?.let { id ->
            // Update the history item with finished state
            historyViewModel.updateHistoryItem(
                id = id,
                endTime = System.currentTimeMillis(),
                completed = true
            )
        }

        Log.d("185", "End of routine")
        withContext(Dispatchers.Main) {
            onRoutineFinished()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
        )

        if (exercise != null) {
            Text(
                text = exercise.exTitle,
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )

            val iconMod = Modifier.padding(18.dp)

            when (val icon = exercise.icon) {
                is ExerciseIcon.DefaultIcon -> DefaultVectorIcon(
                    modifier = iconMod.size(50.dp)
                )

                is ExerciseIcon.RasterIcon -> RasterIcon(
                    modifier = iconMod
                        .fillMaxWidth()
                        .fillMaxHeight(fraction = 0.45f),
                    base64String = icon.imageBase64
                )
            }

            Text(
                text = "Duration: ${exercise.durationSeconds} seconds, Repetitions: ${currentExercise.repetitions}",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = exercise.exDescription,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }
    }
}

fun VerifyDataLinkageIntegrity(
    routines: List<RoutineItem>,
    exercises: List<ExerciseItem>
): List<RoutineItem> {
    return routines.map { routine ->
        val updatedExerciseList = routine.exerciseList.filter { routineExercise ->
            exercises.any { it.id == routineExercise.exerciseId }
        }
        routine.copy(exerciseList = updatedExerciseList.toPersistentList())
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
    var editedExerciseList by remember {
        mutableStateOf(
            routineItem?.exerciseList ?: persistentListOf()
        )
    }
//    SideEffect {
//        if (editedExerciseList.size != 0) {
//            val verifiedExerciseList = VerifyDataLinkageIntegrity(
//                listOf(RoutineItem(exerciseList = editedExerciseList)),
//                exerciseData.excList
//            ).firstOrNull()?.exerciseList
//            if (verifiedExerciseList != null) {
//                editedExerciseList = verifiedExerciseList
//            }
//        }
//    }

    LaunchedEffect(exerciseData) {
        if (editedExerciseList.isNotEmpty()) {
            val verifiedExerciseList = VerifyDataLinkageIntegrity(
                listOf(RoutineItem(exerciseList = editedExerciseList)),
                exerciseData.excList
            ).firstOrNull()?.exerciseList
            if (verifiedExerciseList != null) {
                editedExerciseList = verifiedExerciseList
            }
        }
    }

    var expandedItemId by remember { mutableStateOf<String?>(null) }

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    var textFieldValue by remember { mutableStateOf(TextFieldValue(editedTitle)) }

    LaunchedEffect(Unit) {
        if (editedTitle == "") focusRequester.requestFocus()
        textFieldValue = textFieldValue.copy(selection = TextRange(editedTitle.length))
    }

    var showExerciseSelectionDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val updatedRoutineItem = RoutineItem(
                        id = routineItem?.id ?: UUID.randomUUID().toString(),
                        routineTitle = editedTitle,
                        exerciseList = editedExerciseList,
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
            Box(modifier = Modifier.weight(1f)) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(editedExerciseList, key = { it.id }) { routineExercise ->
                        val exercise =
                            exerciseData.excList.find { it.id == routineExercise.exerciseId }
                        val isExpanded = expandedItemId == routineExercise.id

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
                            description = "Duration: ${exercise?.durationSeconds ?: 10}, " +
                                    "Repetitions: ${routineExercise.repetitions}" +
                                    "\n ${exercise?.exDescription}",
                            onClick = {
                                expandedItemId = if (isExpanded) null else routineExercise.id
                            },
                            onDismissToStart = {
                                // Remove the RoutineExercise from the editedExerciseList
                                editedExerciseList = editedExerciseList.filterNot {
                                    it.id == routineExercise.id
                                }.toPersistentList()
                            },
                            trailingContent = {
                                if (isExpanded) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(end = 8.dp)
                                    ) {
                                        val buttonModifier = Modifier.width(30.dp)

                                        IconButton(
                                            modifier = buttonModifier,
                                            onClick = {
                                                // Decrement repetitions count
                                                val updatedRepetitions =
                                                    (routineExercise.repetitions - 1).coerceAtLeast(
                                                        1
                                                    )
                                                val updatedRoutineExercise =
                                                    routineExercise.copy(repetitions = updatedRepetitions)
                                                editedExerciseList = editedExerciseList.map {
                                                    if (it.id == routineExercise.id) updatedRoutineExercise else it
                                                }.toPersistentList()
                                            }
                                        ) {
                                            Icon(
                                                Icons.Default.Remove,
                                                contentDescription = "Decrease repetitions"
                                            )
                                        }

                                        Text(
                                            text = routineExercise.repetitions.toString(),
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier
                                                .width(24.dp)
                                        )

                                        IconButton(
                                            modifier = buttonModifier,
                                            onClick = {
                                                // Increment repetitions count
                                                val updatedRepetitions =
                                                    (routineExercise.repetitions + 1).coerceAtMost(
                                                        99
                                                    )
                                                val updatedRoutineExercise =
                                                    routineExercise.copy(repetitions = updatedRepetitions)
                                                editedExerciseList = editedExerciseList.map {
                                                    if (it.id == routineExercise.id) updatedRoutineExercise else it
                                                }.toPersistentList()
                                            }
                                        ) {
                                            Icon(
                                                Icons.Default.Add,
                                                contentDescription = "Increase repetitions"
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(8.dp))


                                        IconButton(
                                            modifier = buttonModifier,
                                            onClick = {
                                                val currentIndex =
                                                    editedExerciseList.indexOf(routineExercise)
                                                if (currentIndex > 0) {
                                                    editedExerciseList =
                                                        editedExerciseList.toMutableList().apply {
                                                            add(
                                                                currentIndex - 1,
                                                                removeAt(currentIndex)
                                                            )
                                                        }.toPersistentList()
                                                }
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.ArrowUpward,
                                                contentDescription = "Move Up"
                                            )
                                        }

                                        IconButton(
                                            modifier = buttonModifier,
                                            onClick = {
                                                val currentIndex =
                                                    editedExerciseList.indexOf(routineExercise)
                                                if (currentIndex < editedExerciseList.lastIndex) {
                                                    editedExerciseList =
                                                        editedExerciseList.toMutableList().apply {
                                                            add(
                                                                currentIndex + 1,
                                                                removeAt(currentIndex)
                                                            )
                                                        }.toPersistentList()
                                                }
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.ArrowDownward,
                                                contentDescription = "Move Down"
                                            )
                                        }
                                    }
                                }
                            }
                        )
                    }
                }
            }

            Button(modifier = Modifier
                .align(Alignment.CenterHorizontally), onClick = {
                showExerciseSelectionDialog = true
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
    if (showExerciseSelectionDialog) {
        ExerciseSelectionDialog(
            exercises = exerciseData.excList,
            onExerciseSelected = { selectedExercise ->
                val newRoutineExercise = RoutineExercise(
                    exerciseId = selectedExercise.id,
                    repetitions = 1 // one by default
                )
                editedExerciseList = editedExerciseList.add(newRoutineExercise)
                expandedItemId = newRoutineExercise.id
            },
            onDismiss = {
                showExerciseSelectionDialog = false
            }
        )
    }
}

@Composable
fun ExerciseSelectionDialog(
    exercises: List<ExerciseItem>,
    onExerciseSelected: (ExerciseItem) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            Text(
                text = "Select an Exercise",
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn {
                items(exercises) { exercise ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onExerciseSelected(exercise)
                                onDismiss()
                            }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val iconMod = Modifier.size(24.dp)
                        when (val icon = exercise.icon) {
                            is ExerciseIcon.DefaultIcon -> DefaultVectorIcon(
                                modifier = iconMod
                            )

                            is ExerciseIcon.RasterIcon -> RasterIcon(
                                modifier = iconMod,
                                base64String = icon.imageBase64
                            )
                        }
                        Text(
                            text = exercise.exTitle,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
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
