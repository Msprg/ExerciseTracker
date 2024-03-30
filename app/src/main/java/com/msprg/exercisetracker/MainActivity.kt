package com.msprg.exerciseTracker

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.datastore.dataStore
import com.msprg.exerciseTracker.ui.navigation.AppNavCtl
import com.msprg.exerciseTracker.ui.navigation.dataStore
import com.msprg.exerciseTracker.ui.theme.ExerciseTrackerTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExerciseTrackerTheme {
                AppNavCtl()
            }
        }
    }
}
