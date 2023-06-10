package com.msprg.exercisetracker

import AddExerciseDialogFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExercisesActivity : AppCompatActivity() {

    private lateinit var DB: ExerciseDAO
    private lateinit var exerciseList: ListView
    private lateinit var addButton: Button
    private lateinit var adapter: ArrayAdapter<ExerciseData>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercises)

        val exercises = mutableListOf<ExerciseData>()

        DB = ExerciseDatabase.getInstance(applicationContext).exerciseDao()
        // Fetch exercise data from the Room database
        lifecycleScope.launch {
            exercises.addAll(DB.getAllExercises())
            adapter.notifyDataSetChanged()
        }

        exerciseList = findViewById(R.id.exerciseList)
        addButton = findViewById(R.id.addButton)

        addButton.setOnClickListener {
            val dialogFragment = AddExerciseDialogFragment()
            dialogFragment.show(supportFragmentManager, "AddExerciseDialog")
        }


        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, exercises)
        exerciseList.adapter = adapter

        exerciseList.setOnItemClickListener { parent, view, position, id ->
            val element = parent.getItemAtPosition(position) // The item that was clicked
        }

        exerciseList.setOnItemLongClickListener { item, view, int, long ->
            lifecycleScope.launch {
                DB.delete(exercises[int])
            }
            //adapter.remove(item.adapter.getItem(int) as String?)
            adapter.remove(exercises[int])
            adapter.notifyDataSetChanged()
            true
        }


    }

    fun addExercise(exerciseName: String) {
        val addee = ExerciseData(null, exerciseName, "")
        // Add the exercise to the adapter and update the list
        adapter.add(addee)
        adapter.notifyDataSetChanged()

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                DB.insert(addee)
            }
        }
    }
}