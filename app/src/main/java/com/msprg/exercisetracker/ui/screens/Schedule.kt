package com.msprg.exerciseTracker.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EventAvailable
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.msprg.exerciseTracker.ui.components.RowItemsMockup
import com.msprg.exerciseTracker.ui.navigation.AppNavCtl
import com.msprg.exerciseTracker.ui.navigation.Screens
import com.msprg.exerciseTracker.ui.theme.ExerciseTrackerTheme

@Composable
fun ScheduleScreen() {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { },
                shape = CircleShape
            ) {
                Icon(Icons.Rounded.Verified, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
        ) {
            RowItemsMockup(icon = Icons.Outlined.EventAvailable)
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ScheduleScreenPrew() {
    ExerciseTrackerTheme {
        AppNavCtl(Screens.ScheduleScreen)
    }
}