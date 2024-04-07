package com.msprg.exerciseTracker.ui.screens

import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
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
import com.msprg.exerciseTracker.ui.components.RowItem
import com.msprg.exerciseTracker.ui.navigation.AppNavCtl
import com.msprg.exerciseTracker.ui.navigation.Screens
import com.msprg.exerciseTracker.ui.theme.ExerciseTrackerTheme
import java.io.ByteArrayOutputStream

fun encodeImageToBase64(bitmap: Bitmap): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

fun decodeBase64ToImage(encodedString: String): Bitmap {
    val decodedBytes = Base64.decode(encodedString, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
}

@Composable
fun ExercisesScreen(
    viewModel: MainActivityViewModel = MainActivityViewModel(ExTrApplication.datastoremodule),
//    exerciseData: ExercisesList,
//    viewModel: MainActivityViewModel,
    navCtl: NavController
) {
    var showAddDialog by remember { mutableStateOf(false) }
    val exerciseData by viewModel.exerciseDataFlow.collectAsState(initial = ExercisesList())
//	val scope = rememberCoroutineScope()

    if (showAddDialog) {
        AddExerciseDialog(
            onDismiss = { showAddDialog = false }
        ) { title: String, description: String, bitmap: Bitmap? ->
            val icon = if (bitmap != null) {
                val base64String = encodeImageToBase64(bitmap)
                ExerciseIcon.RasterIcon(base64String)
            } else {
                ExerciseIcon.DefaultIcon
            }
            viewModel.addExerciseItem(
                icon = icon,
                title, description
            )
        }
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
                when (val icon = exerciseItem.icon) {
                    is ExerciseIcon.DefaultIcon -> {
                        RowItem(
                            icon = {
                                GetDefaultVectorIcon()
                            },
                            title = exerciseItem.exTitle,
                            description = exerciseItem.exDescription,
                            onClick = {
                                navCtl.navigate("${Screens.ExerciseItemViewScreen.name}/$index")
                            },
                            onLongClick = {
                                viewModel.deleteExerciseItem(index)
                            }
                        )
                    }

                    is ExerciseIcon.RasterIcon -> {
                        val bitmap = try {
                            decodeBase64ToImage(icon.imageBase64)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            null
                        }

                        RowItem(
                            icon = {
                                if (bitmap != null) {
                                    Image(
                                        bitmap = bitmap.asImageBitmap(),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(55.dp)
                                            .padding(start = 8.dp)
                                    )
                                } else {
                                    GetDefaultVectorIcon()
                                }
                            },
                            title = exerciseItem.exTitle,
                            description = exerciseItem.exDescription,
                            onClick = {
                                navCtl.navigate("${Screens.ExerciseItemViewScreen.name}/$index")
                            },
                            onLongClick = {
                                viewModel.deleteExerciseItem(index)
                            }
                        )
                    }
                }
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
private fun GetDefaultVectorIcon() {
    Icon(
        imageVector = Icons.Default.FitnessCenter,
        contentDescription = null,
        modifier = Modifier
            .size(55.dp)
            .padding(start = 8.dp)
    )
}

@Composable
fun AddExerciseDialog(
    onDismiss: () -> Unit,
    onSave: (String, String, Bitmap?) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedImage by remember { mutableStateOf<Bitmap?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                val inputStream = ExTrApplication.appContext.contentResolver.openInputStream(it)
                selectedImage = BitmapFactory.decodeStream(inputStream)
            }
        }
    )
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
                    maxLines = 8
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextButton(
                    onClick = {
                        launcher.launch("image/*")
                    }
                )
                {
                    Text("Pick Image")
                }
                selectedImage?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "Selected Image",
                        modifier = Modifier
                            .size(100.dp)
                            .padding(top = 8.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onSave(title, description, selectedImage)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseItemViewScreen(
    exerciseItem: ExerciseItem,
    onBackPressed: () -> Unit
) {
    var showFullscreenImage by remember { mutableStateOf(false) }
    var fullscreenBitmap by remember { mutableStateOf<Bitmap?>(null) }

    if (showFullscreenImage && fullscreenBitmap != null) {
        ShowImage(
            showFullscreenImage = showFullscreenImage,
            fullscreenBitmap = fullscreenBitmap,
            onDismiss = { showFullscreenImage = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = exerciseItem.exTitle) },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            when (val icon = exerciseItem.icon) {
                is ExerciseIcon.DefaultIcon -> {
                    Icon(
                        imageVector = Icons.Default.FitnessCenter,
                        contentDescription = null,
                        modifier = Modifier
                            .size(200.dp)
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 16.dp)
                    )
                }

                is ExerciseIcon.RasterIcon -> {
                    val bitmap = try {
                        decodeBase64ToImage(icon.imageBase64)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        null
                    }
                    if (bitmap != null) {
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier
                                .size(200.dp)
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 16.dp)
                                .clickable {
                                    fullscreenBitmap = bitmap
                                    showFullscreenImage = true
                                }
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.FitnessCenter,
                            contentDescription = null,
                            modifier = Modifier
                                .size(200.dp)
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 16.dp)
                        )
                    }
                }
            }
            Text(
                text = exerciseItem.exDescription,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}

@Composable
private fun ShowImage(
    showFullscreenImage: Boolean,
    fullscreenBitmap: Bitmap?,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismiss
                )
        ) {
            Image(
                bitmap = fullscreenBitmap!!.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.Center)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        // Do nothing when clicking on the image
                    }
            )
        }
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ExercisesScreenPrew() {
    ExerciseTrackerTheme {
        AppNavCtl(Screens.ExercisesScreen)
    }
}
