package com.msprg.exercisetracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class ViewExerciseFragment : Fragment() {
    private lateinit var exerciseName: String
    private lateinit var exerciseDescription: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            exerciseName = it.getString(ARG_EXERCISE_NAME, "")
            exerciseDescription = it.getString(ARG_EXERCISE_DESCRIPTION, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_view_exercise, container, false)

        val nameTextView: TextView = rootView.findViewById(R.id.nameTextView)
        val descriptionTextView: TextView = rootView.findViewById(R.id.descriptionTextView)

        // Set the exercise name
        exerciseName = ""
        nameTextView.text = exerciseName

        // Set the exercise description
        exerciseDescription = ""
        descriptionTextView.text = exerciseDescription

        return rootView
    }

    companion object {
        private const val ARG_EXERCISE_NAME = "arg_exercise_name"
        private const val ARG_EXERCISE_DESCRIPTION = "arg_exercise_description"

        fun newInstance(exerciseName: String, exerciseDescription: String): ViewExerciseFragment {
            val fragment = ViewExerciseFragment()
            val args = Bundle().apply {
                putString(ARG_EXERCISE_NAME, exerciseName)
                putString(ARG_EXERCISE_DESCRIPTION, exerciseDescription)
            }
            fragment.arguments = args
            return fragment
        }
    }
}
