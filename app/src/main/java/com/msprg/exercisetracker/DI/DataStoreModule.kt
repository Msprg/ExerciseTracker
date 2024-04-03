package com.msprg.exerciseTracker.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.msprg.exerciseTracker.data.ExercisesList
import com.msprg.exerciseTracker.data.PersistUserdataSerializer

private const val DATA_STORE_FILE_NAME = "Userdata.json"

interface IFDataStoreModule {
    val protoDS: DataStore<ExercisesList>

}

class DataStoreModuleImpl(
    private val appContext: Context
) : IFDataStoreModule {
    override val protoDS: DataStore<ExercisesList> by lazy {
        DataStoreFactory.create(
            serializer = PersistUserdataSerializer,
//            produceFile = { appContext.dataStoreFile(DATA_STORE_FILE_NAME) },
            corruptionHandler = null
        ) {
            appContext.dataStoreFile(DATA_STORE_FILE_NAME)
        }
    }
}