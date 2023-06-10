package com.msprg.exercisetracker

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ExerciseDAO {
    @Insert
    suspend fun insert(exercise: ExerciseData)

    @Update
    suspend fun update(exercise: ExerciseData)

    @Delete
    suspend fun delete(exercise: ExerciseData)

    @Query("SELECT * FROM exercises")
    suspend fun getAllExercises(): List<ExerciseData>
}