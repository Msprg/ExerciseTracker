package com.msprg.exerciseTracker.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msprg.exerciseTracker.data.RoutineItem
import com.msprg.exerciseTracker.data.RoutinesList
import com.msprg.exerciseTracker.di.IFDataStoreModule
import kotlinx.collections.immutable.mutate
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class RoutinesViewModel(
    private val dsModule: IFDataStoreModule
) : ViewModel() {
    val routinesDataFlow: Flow<RoutinesList> = dsModule.routinesDS.data

    fun addRoutine(routine: RoutineItem) {
        viewModelScope.launch {
            dsModule.routinesDS.updateData { currentList ->
                currentList.copy(
                    routineList = currentList.routineList.mutate {
                        it.add(routine)
                    }
                )
            }
        }
    }

    fun updateRoutine(updatedRoutine: RoutineItem) {
        viewModelScope.launch {
            dsModule.routinesDS.updateData { currentList ->
                currentList.copy(
                    routineList = currentList.routineList.map { routine ->
                        if (routine.id == updatedRoutine.id) updatedRoutine else routine
                    }.toPersistentList()
                )
            }
        }
    }

    fun deleteRoutine(routineId: String) {
        viewModelScope.launch {
            dsModule.routinesDS.updateData { currentList ->
                currentList.copy(
                    routineList = currentList.routineList.filterNot { it.id == routineId }
                        .toPersistentList()
                )
            }
        }
    }
}
