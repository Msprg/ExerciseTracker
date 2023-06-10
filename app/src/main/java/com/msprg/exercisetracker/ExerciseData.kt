package com.msprg.exercisetracker

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class ExerciseData(@PrimaryKey(autoGenerate = true)
                        @ColumnInfo(name = "uid")
                        val uid: Long? = null,
                        val name: String,
                        val description: String
                        ) {
    override fun toString(): String {return this.name}

}
