package com.msprg.exerciseTracker.data

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class RoutinesList(
    @Serializable(with = PersistentListSerializer::class)
    val routineList: PersistentList<RoutineItem> = persistentListOf()
)

@Serializable
data class RoutineItem(
    val id: String = UUID.randomUUID().toString(),
    val routineTitle: String = "",
    val routineDescription: String = "",
    @Serializable(with = PersistentListSerializer::class)
    val exerciseList: PersistentList<RoutineExercise> = persistentListOf(),
    val repetitions: Int = 1
)

@Serializable
data class RoutineExercise(
    val exerciseId: String,
    val duration: Int = 0,
    val repetitions: Int = 1
)
