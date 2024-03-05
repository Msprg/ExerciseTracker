package com.msprg.exercisetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview

//class MainActivity : ComponentActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//    }
//
//
//    fun openExercises(view: View) {
//        val intent = Intent(this, ExercisesActivity::class.java)
//        startActivity(intent)
//    }
//
//    fun openRoutines(view: View) {
//        val intent = Intent(this, RoutinesActivity::class.java)
//        startActivity(intent)
//    }
//
//    fun openSchedule(view: View) {
//        val intent = Intent(this, ScheduleActivity::class.java)
//        startActivity(intent)
//    }
//
//    fun openHistory(view: View) {
//        val intent = Intent(this, HistoryActivity::class.java)
//        startActivity(intent)
//    }
//}



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
//        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            HistoryScreen()
        }
    }
}


@Composable
fun HistoryScreen() {
    val context = LocalContext.current
    // Simulate CalendarView functionality
//    val selectedDate = remember { mutableStateOf(Calendar.getInstance()) }
//
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        // Placeholder for CalendarView
//        Text(text = "Selected Date: ${selectedDate.value.time}")
//    }
//
//    // Simulate date change listener
//    LaunchedEffect(key1 = true) {
//        // Handle date selection if needed
//        // You can retrieve the selected date (year, month, dayOfMonth) here
//    }
}

@Preview
@Composable
fun PreviewHistoryScreen() {
    HistoryScreen()
}