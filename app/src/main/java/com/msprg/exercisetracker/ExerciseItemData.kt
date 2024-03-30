package com.msprg.exerciseTracker

import kotlinx.serialization.Serializable

@Serializable
data class ExerciseItemData(
    val exTitle: String,
    val exDescription: String
)


