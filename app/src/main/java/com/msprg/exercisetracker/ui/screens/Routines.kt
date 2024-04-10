package com.msprg.exerciseTracker.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.EventRepeat
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.msprg.exerciseTracker.ExTrApplication
import com.msprg.exerciseTracker.data.RoutineItem
import com.msprg.exerciseTracker.data.RoutinesList
import com.msprg.exerciseTracker.ui.components.RowItem
import com.msprg.exerciseTracker.ui.navigation.AppNavCtl
import com.msprg.exerciseTracker.ui.navigation.Screens
import com.msprg.exerciseTracker.ui.theme.ExerciseTrackerTheme
import com.msprg.exerciseTracker.ui.viewmodels.RoutinesViewModel

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
//                    navCtl.navigate(Screens.RoutineItemEditScreen.name)
                    viewModel.addRoutine(
                        RoutineItem()
                    )
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

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun RoutinesScreenPrew() {
    ExerciseTrackerTheme {
        AppNavCtl(Screens.RoutinesScreen)
    }
}
