package com.msprg.exercisetracker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.msprg.exercisetracker.ui.theme.ExerciseTrackerTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    fun openExercises(view: View) {
        val intent = Intent(this, ExercisesActivity::class.java)
        startActivity(intent)
    }

    fun openRoutines(view: View) {
        val intent = Intent(this, RoutinesActivity::class.java)
        startActivity(intent)
    }

    fun openSchedule(view: View) {
        val intent = Intent(this, ScheduleActivity::class.java)
        startActivity(intent)
    }

    fun openHistory(view: View) {
        val intent = Intent(this, HistoryActivity::class.java)
        startActivity(intent)
    }
}
