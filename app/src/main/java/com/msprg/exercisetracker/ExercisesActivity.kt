package com.msprg.exercisetracker

import AddExerciseDialogFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.lifecycle.lifecycleScope
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExercisesActivity : AppCompatActivity() {

    private lateinit var DB: ExerciseDAO
    private lateinit var exerciseList: ListView
    private lateinit var addButton: FloatingActionButton
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
            val ex = exercises[position] // Access the clicked item directly from the list
            // Handle the item click event
            val fragment = ViewExerciseFragment.newInstance(ex.name, ex.description)

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit()

        }

        exerciseList.setOnItemLongClickListener { item, view, int, long ->
            //val selectedExercise = exercises[int] // Access the long clicked item directly from the list
            val selectedExercise = adapter.getItem(int)
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    if (selectedExercise != null) {
                        selectedExercise.uid?.let { DB.deleteByUid(it) }
                    }
                }
            }
            adapter.remove(selectedExercise)
            adapter.notifyDataSetChanged()
            true
        }
    }

    fun addExercise(exerciseName: String) {
        val exercise = ExerciseData(name = exerciseName, description = "")

        lifecycleScope.launch {
            val insertedId = withContext(Dispatchers.IO) {
                DB.insert(exercise)
            }

            val updatedExercise = exercise.copy(uid = insertedId)  // Create a new instance with updated uid

            updateExerciseList(updatedExercise)  // Update the list of exercises on the background thread
        }
    }

    private suspend fun updateExerciseList(updatedExercise: ExerciseData) {
        withContext(Dispatchers.Main) {
            adapter.add(updatedExercise)
            adapter.notifyDataSetChanged()
        }
    }
}
