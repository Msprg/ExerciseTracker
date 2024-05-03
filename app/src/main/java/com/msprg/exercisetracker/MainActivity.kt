package com.msprg.exerciseTracker

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.msprg.exerciseTracker.ui.navigation.AppNavCtl
import com.msprg.exerciseTracker.ui.theme.ExerciseTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExerciseTrackerTheme {
                AppNavCtl()
                Log.d("MainActivity", "HELLO")
            }
        }
    }
}
