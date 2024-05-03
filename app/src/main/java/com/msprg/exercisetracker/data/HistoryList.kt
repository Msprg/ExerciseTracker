package com.msprg.exerciseTracker.data

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable
import java.util.UUID


@Serializable
data class HistoryList(
    @Serializable(with = PersistentListSerializer::class)
    val historyList: PersistentList<HistoryItem> = persistentListOf()
)

@Serializable
data class HistoryItem(
    val id: String = UUID.randomUUID().toString(),
    val routineId: String,
    val routineTitle: String,
    val startTime: Long,
    val endTime: Long,
    val completed: Boolean
)
