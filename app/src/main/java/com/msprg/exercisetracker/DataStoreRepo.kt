package com.msprg.exerciseTracker

import androidx.datastore.core.DataStore

//val Context.dataStore by dataStore("Userdata.json", PersistUserdataSerializer)

class DataStoreRepo(private val dataStore: DataStore<ExerciseItemData>) {
    private suspend fun setNumber(num: Int) {
        dataStore.updateData {
            it.copy(
                exTitle = "Exercise Title number $num",
                exDescription = "Exercise Description $num"
            )
        }
    }
}