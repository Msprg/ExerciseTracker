package com.msprg.exercisetracker

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ExerciseData::class], version = 1)
abstract class ExerciseDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDAO

    companion object {
        @Volatile
        private var INSTANCE: ExerciseDatabase? = null

        fun getInstance(context: Context): ExerciseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExerciseDatabase::class.java,
                    "exercise_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
