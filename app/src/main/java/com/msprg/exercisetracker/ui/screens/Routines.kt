package com.msprg.exerciseTracker.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.msprg.exerciseTracker.ui.theme.ExerciseTrackerTheme

@Composable
fun RoutinesScreen() {
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Routines_CONTENT", fontSize = 32.sp)
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun RoutinesScreenPrew() {
    ExerciseTrackerTheme {
        RoutinesScreen()
    }
}