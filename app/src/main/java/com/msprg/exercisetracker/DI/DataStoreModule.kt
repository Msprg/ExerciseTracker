package com.msprg.exerciseTracker.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.msprg.exerciseTracker.data.ExercisesList
import com.msprg.exerciseTracker.data.PersistExercisesListSerializer
import com.msprg.exerciseTracker.data.PersistRoutinesListSerializer
import com.msprg.exerciseTracker.data.RoutinesList

private const val EXERCISES_DATA_STORE = "Userdata.json"
private const val ROUTINES_DATA_STORE = "RoutinesData.json"

interface IFDataStoreModule {
    val exercisesDS: DataStore<ExercisesList>
    val routinesDS: DataStore<RoutinesList>
}

class DataStoreModuleImpl(
    private val appContext: Context
) : IFDataStoreModule {
    override val exercisesDS: DataStore<ExercisesList> by lazy {
        DataStoreFactory.create(
            serializer = PersistExercisesListSerializer,
            corruptionHandler = null
        ) {
            appContext.dataStoreFile(EXERCISES_DATA_STORE)
        }
    }

    override val routinesDS: DataStore<RoutinesList> by lazy {
        DataStoreFactory.create(
            serializer = PersistRoutinesListSerializer,
            corruptionHandler = null
        ) {
            appContext.dataStoreFile(ROUTINES_DATA_STORE)
        }
    }
}