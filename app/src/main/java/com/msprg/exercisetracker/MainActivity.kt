package com.msprg.exerciseTracker

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.datastore.dataStore
import com.msprg.exerciseTracker.ui.navigation.AppNavCtl
import com.msprg.exerciseTracker.ui.theme.ExerciseTrackerTheme


val Context.dataStore by dataStore("Userdata.json", PersistUserdataSerializer)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExerciseTrackerTheme {
                AppNavCtl(dataStore = dataStore)
				Log.d("MainActivity", "HELLO")
            }
        }
    }
}
