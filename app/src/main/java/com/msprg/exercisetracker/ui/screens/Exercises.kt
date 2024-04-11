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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
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
import java.util.UUID

object ImageUtils {
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
}

@Composable
fun ExercisesScreen(
    navCtl: NavController,
    viewModel: MainActivityViewModel = MainActivityViewModel(ExTrApplication.datastoremodule)
) {
    val exerciseData by viewModel.exerciseDataFlow.collectAsState(initial = ExercisesList())

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navCtl.navigate(Screens.ExerciseItemEditScreen.name)
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
            val iconMod = Modifier
                .size(55.dp)
                .padding(start = 8.dp)
            items(exerciseData.excList, key = { it.id }) { exerciseItem ->
                RowItem(
                    icon = {
                        when (val icon = exerciseItem.icon) {
                            is ExerciseIcon.DefaultIcon -> DefaultVectorIcon(iconMod)
                            is ExerciseIcon.RasterIcon -> RasterIcon(
                                iconMod,
                                base64String = icon.imageBase64
                            )
                        }
                    },
                    title = exerciseItem.exTitle,
                    description = "${exerciseItem.id} ${exerciseItem.exDescription}",
                    onClick = {
                        navCtl.navigate("${Screens.ExerciseItemViewScreen.name}/${exerciseItem.id}")
                    },
                    onDismissToStart = {
                        viewModel.deleteExerciseItem(exerciseItem.id)
                    }
                )
            }
        }
    }
}

@Composable
fun DefaultVectorIcon(modifier: Modifier = Modifier) {
    Icon(
        imageVector = Icons.Default.FitnessCenter,
        contentDescription = null,
        modifier = modifier
    )
}

@Composable
fun RasterIcon(modifier: Modifier = Modifier, base64String: String) {
    val bitmap = try {
        ImageUtils.decodeBase64ToImage(base64String)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
    bitmap?.let {
        Image(
            bitmap = it.asImageBitmap(),
            contentDescription = null,
            modifier = modifier
        )
    } ?: DefaultVectorIcon()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseItemViewScreen(
    exerciseItem: ExerciseItem,
    onBackPressed: () -> Unit,
    onEditPressed: () -> Unit
) {
    var showFullscreenImage by remember { mutableStateOf(false) }
    var fullscreenBitmap by remember { mutableStateOf<Bitmap?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = exerciseItem.exTitle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onEditPressed,
                shape = CircleShape
            ) {
                Icon(Icons.Rounded.Edit, contentDescription = "Edit")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            val iconMod =
                Modifier
                    .size(200.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp)

            when (val icon = exerciseItem.icon) {
                is ExerciseIcon.DefaultIcon -> DefaultVectorIcon(modifier = iconMod)
                is ExerciseIcon.RasterIcon -> {
                    val bitmap = ImageUtils.decodeBase64ToImage(icon.imageBase64)
                    RasterIcon(
                        modifier = iconMod
                            .clickable {
                                fullscreenBitmap = bitmap
                                showFullscreenImage = true
                            },
                        icon.imageBase64
                    )
                }
            }

            Text(
                text = "Duration: ${exerciseItem.durationSeconds} (seconds)",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            )

            Text(
                text = exerciseItem.exDescription,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
            )
        }
    }

    if (showFullscreenImage && fullscreenBitmap != null) {
        ShowFullscreenImage(
            bitmap = fullscreenBitmap,
            onDismiss = { showFullscreenImage = false }
        )
    }
}

@Composable
private fun ShowFullscreenImage(
    bitmap: Bitmap?,
    onDismiss: () -> Unit
) {
    bitmap?.let {
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
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {} // intended behaviour
                        )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseItemEditScreen(
    exerciseItem: ExerciseItem?,
    onBackPressed: () -> Unit,
    onSavePressed: (ExerciseItem) -> Unit
) {
    var editedTitle by remember { mutableStateOf(exerciseItem?.exTitle ?: "") }
    var editedDescription by remember { mutableStateOf(exerciseItem?.exDescription ?: "") }
    var editedDuration by remember {
        mutableStateOf(
            exerciseItem?.durationSeconds?.toString() ?: "10"
        )
    }
    var editedBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    var textFieldValue by remember { mutableStateOf(TextFieldValue(editedTitle)) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                val inputStream = ExTrApplication.appContext.contentResolver.openInputStream(it)
                editedBitmap = BitmapFactory.decodeStream(inputStream)
            }
        }
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        textFieldValue = textFieldValue.copy(selection = TextRange(editedTitle.length))
        editedBitmap = when (val icon = exerciseItem?.icon) {
            is ExerciseIcon.DefaultIcon -> null
            is ExerciseIcon.RasterIcon -> ImageUtils.decodeBase64ToImage(icon.imageBase64)
            else -> null
        }
    }

    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    OutlinedTextField(
//                        value = textFieldValue,
//                        onValueChange = {
//                            textFieldValue = it
//                            editedTitle = it.text
//                        },
//                        label = { Text("Title") },
//                        singleLine = true,
//                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
//                        keyboardActions = KeyboardActions(onDone = {
//                            focusManager.clearFocus()
//                            keyboardController?.hide()
//                        }),
//                        modifier = Modifier
//                            .focusRequester(focusRequester)
//                            .fillMaxWidth()
//                    )
//                },
//                navigationIcon = {
//                    IconButton(onClick = onBackPressed) {
//                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
//                    }
//                }
//            )
//        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val updatedExerciseItem = ExerciseItem(
                        id = exerciseItem?.id ?: UUID.randomUUID().toString(),
                        exTitle = editedTitle,
                        exDescription = editedDescription,
                        durationSeconds = editedDuration.toInt(),
                        icon = editedBitmap?.let {
                            ExerciseIcon.RasterIcon(ImageUtils.encodeImageToBase64(it))
                        } ?: ExerciseIcon.DefaultIcon
                    )
                    onSavePressed(updatedExerciseItem)
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
                .verticalScroll(rememberScrollState())
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

            val iconMod =
                Modifier
                    .size(200.dp)
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .padding(top = 16.dp)

            if (editedBitmap != null) {
                Image(
                    bitmap = editedBitmap!!.asImageBitmap(),
                    contentDescription = null,
                    modifier = iconMod
                        .clickable { editedBitmap = null }
                )
            } else {
                Icon(
                    imageVector = Icons.Default.FitnessCenter,
                    contentDescription = null,
                    modifier = iconMod
                        .clickable { launcher.launch("image/*") }
                )
            }

            OutlinedTextField(
                value = editedDuration,
                onValueChange = { newValue ->
                    editedDuration = newValue.filter { it.isDigit() }
                },
                label = { Text("Duration of Exercise (seconds)") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier
                    .align(Alignment.End)
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            OutlinedTextField(
                value = editedDescription,
                onValueChange = { editedDescription = it },
                label = { Text("Description") },
                minLines = 13,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
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
