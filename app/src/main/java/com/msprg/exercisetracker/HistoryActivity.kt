package com.msprg.exercisetracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CalendarView

class HistoryActivity : AppCompatActivity() {

    private lateinit var calendarView: CalendarView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        calendarView = findViewById(R.id.calendarView)
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // Handle date selection if needed
            // You can retrieve the selected date (year, month, dayOfMonth) here
        }

        // Get the current date
        val currentDate = System.currentTimeMillis()

        // Set the current date as the highlighted date in the calendar
        calendarView.setDate(currentDate, false, true)
    }
}
