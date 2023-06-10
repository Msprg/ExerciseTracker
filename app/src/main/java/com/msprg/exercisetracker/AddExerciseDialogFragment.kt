import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.msprg.exercisetracker.ExercisesActivity
import com.msprg.exercisetracker.R

class AddExerciseDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_add_exercise, null)

        val exerciseEditText = dialogView.findViewById<EditText>(R.id.editTextExercise)

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogView)
            .setTitle("Add Exercise")
            .setPositiveButton("Add") { dialog, _ ->
                val exerciseName = exerciseEditText?.text.toString()
                addExercise(exerciseName)
            }
            .setNegativeButton("Cancel", null)

        return builder.create()
    }


    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        // Handle dialog cancellation if needed
    }

    private fun addExercise(exerciseName: String) {
        // Add the exercise to the list
        val exercisesActivity = activity as ExercisesActivity
        exercisesActivity.addExercise(exerciseName)
    }
}
