package com.msprg.exerciseTracker

import android.util.Log
import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.catch
import java.io.IOException
import java.util.concurrent.Flow

//val Context.dataStore by dataStore("Userdata.json", PersistUserdataSerializer)

class DataStoreRepo(private val dataStore: DataStore<ExerciseItemData>) {

	private val TAG: String = "DataStoreRepo"

//	val ExerciseItemDataFlow: Flow<ExerciseItemData> = dataStore.data
//		.catch { exception ->
//			// dataStore.data throws an IOException when an error is encountered when reading data
//			if (exception is IOException) {
//				Log.e(TAG, "Error reading sort order preferences.", exception)
//			} else {
//				throw exception
//			}
//		}
    public suspend fun setNumber(num: Int) {
        dataStore.updateData {
            it.copy(
                exTitle = "Exercise Title number $num",
                exDescription = "Exercise Description $num"
            )
        }
    }
}
