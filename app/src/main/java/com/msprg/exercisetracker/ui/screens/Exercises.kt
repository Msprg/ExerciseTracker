package com.msprg.exerciseTracker.ui.screens

import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.msprg.exerciseTracker.ExTrApplication
import com.msprg.exerciseTracker.MainActivityViewModel
import com.msprg.exerciseTracker.data.ExerciseIcon
import com.msprg.exerciseTracker.data.ExercisesList
import com.msprg.exerciseTracker.ui.components.RowItem
import com.msprg.exerciseTracker.ui.navigation.AppNavCtl
import com.msprg.exerciseTracker.ui.navigation.Screens
import com.msprg.exerciseTracker.ui.theme.ExerciseTrackerTheme

fun decodeBase64ToImage(encodedString: String): Bitmap {
    val decodedBytes = Base64.decode(encodedString, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
}


@Composable
fun ExercisesScreen(
    viewModel: MainActivityViewModel = MainActivityViewModel(ExTrApplication.datastoremodule)
) {
    var showAddDialog by remember { mutableStateOf(false) }
    val exerciseData by viewModel.exerciseDataFlow.collectAsState(initial = ExercisesList())
//	val scope = rememberCoroutineScope()

    if (showAddDialog) {
        AddExerciseDialog(
            onDismiss = { showAddDialog = false },
            onSave = { title, description ->
                // Handle saving the exercise item
                // You can call a function in your ViewModel to save the data
                viewModel.addExerciseItem(
                    icon = ExerciseIcon.VectorIcon(),
                    title, description
                )
            }
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showAddDialog = true
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
            items(exerciseData.excList.size) { index ->
                val exerciseItem = exerciseData.excList[index]
//                exerciseData.excList.forEachIndexed { index, exerciseItem ->
                when (val icon = exerciseItem.icon) {
                    is ExerciseIcon.VectorIcon -> {
                        val imageVector = when (icon.iconName) {
                            "FitnessCenter" -> Icons.Default.FitnessCenter
                            else -> Icons.Default.FitnessCenter
                        }
                        RowItem(
                            icon = {
                                Icon(
                                    imageVector = imageVector,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(55.dp)
                                        .padding(start = 8.dp)
                                )
                            },
                            title = exerciseItem.exTitle,
                            description = exerciseItem.exDescription,
                            onLongClick = {
                                viewModel.deleteExerciseItem(index)
                            }
                        )
//
//                        var fitnessCenterInstance = Icons.Default.Square
//                        try {
//                            val iconsClass = Icons::class.java
//                            val filledField = iconsClass.getField("Filled")
//                            val filledObject = filledField.get(null)
//                            val fitnessCenterField = filledObject::class.java.getField("FitnessCenter")
////                            val fitnessCenterInstance = fitnessCenterField.get(null) as ImageVector
//                            fitnessCenterInstance = fitnessCenterField.get(null) as ImageVector
//                            // Use the fitnessCenterInstance as needed
//                        } catch (e: Exception) {
//                            // Handle any exceptions that may occur during reflection
//                            e.printStackTrace()
//                        }

//                        val imageVector = Icons.Filled::class.java.fields.firstOrNull {
//                            it.name == icon.iconName
//                        }?.get(null) as? ImageVector
//                            ?: throw IllegalArgumentException("Unknown ImageVector: ${icon.iconName}")
                    }

                    is ExerciseIcon.RasterIcon -> {
                        val bitmap = decodeBase64ToImage(icon.imageBase64)
                        RowItem(
//                            icon = bitmap,
                            title = exerciseItem.exTitle,
                            description = exerciseItem.exDescription
                        )
                    }
                }
//                }
            }
//            exerciseData.ExcList.forEach {
//                RowItem(
////                    icon = it.icon. ,
//                    title = it.exTitle,
//                    description = it.exDescription
//                )
//            }
//            RowItemsMockup(icon = Icons.Default.FitnessCenter,
//                title = exerciseData.exTitle,
//                description = exerciseData.exDescription
//			)
        }
    }
}

@Composable
fun AddExerciseDialog(
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "New Exercise")
        },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    maxLines = 5
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onSave(title, description)
                    onDismiss()
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ExercisesScreenPrew() {
    ExerciseTrackerTheme {
        AppNavCtl(Screens.ExercisesScreen)
    }
}
