package com.msprg.exerciseTracker.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.msprg.exerciseTracker.ui.components.RowItemsMockup
import com.msprg.exerciseTracker.ui.navigation.AppNavCtl
import com.msprg.exerciseTracker.ui.navigation.Screens
import com.msprg.exerciseTracker.ui.theme.ExerciseTrackerTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navCtl: NavController) {
    Scaffold(modifier = Modifier.fillMaxSize(),
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                val datePickerState = rememberDatePickerState()
                DatePicker(
                    state = datePickerState,
                    modifier = Modifier.padding(16.dp),
                    title = null,
                    headline = null,
                    showModeToggle = false
                )
                Text(
                    "Selected date timestamp: ${datePickerState.selectedDateMillis ?: "no selection"}",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontSize = 19.sp
                )
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start,
                ) {
                    RowItemsMockup(
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.History,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(55.dp)
                                    .padding(start = 8.dp)
                            )
                        }
                    )
                }
            }
        }
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HistoryScreenPrew() {
    ExerciseTrackerTheme {
        AppNavCtl(Screens.HistoryScreen)
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HistoryScreenPrewWireframe() {
    ExerciseTrackerTheme {
        AppNavCtl(Screens.HistoryScreen)
    }
}

