package com.msprg.exercisetracker

import AddExerciseDialogFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView

class ExercisesActivity : AppCompatActivity() {

    private lateinit var exerciseList: ListView
    private lateinit var addButton: Button
    private lateinit var adapter: ArrayAdapter<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercises)

        exerciseList = findViewById(R.id.exerciseList)
        addButton = findViewById(R.id.addButton)

        val exercises = mutableListOf<String>()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, exercises)
        exerciseList.adapter = adapter

        addButton.setOnClickListener {
            // Handle the click event for the "+" button
            showAddExerciseDialog()
        }
    }
    private fun showAddExerciseDialog() {
        val dialogFragment = AddExerciseDialogFragment()
        dialogFragment.show(supportFragmentManager, "AddExerciseDialog")
    }

    fun addExercise(exerciseName: String) {
        // Add the exercise to the adapter and update the list
        adapter.add(exerciseName)
        adapter.notifyDataSetChanged()
    }

}