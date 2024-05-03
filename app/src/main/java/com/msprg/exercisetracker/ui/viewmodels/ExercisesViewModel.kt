package com.msprg.exerciseTracker.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msprg.exerciseTracker.data.ExerciseIcon
import com.msprg.exerciseTracker.data.ExerciseItem
import com.msprg.exerciseTracker.data.ExercisesList
import com.msprg.exerciseTracker.di.IFDataStoreModule
import kotlinx.collections.immutable.mutate
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ExercisesViewModel(
    private val dsModule: IFDataStoreModule
) : ViewModel() {
    val exerciseDataFlow: Flow<ExercisesList> = dsModule.exercisesDS.data

    fun addExerciseItem(
        icon: ExerciseIcon,
        title: String,
        description: String
    ) {
        viewModelScope.launch {
            dsModule.exercisesDS.updateData {
                it.copy(
                    excList = it.excList.mutate {
                        it.add(
                            element = ExerciseItem(
                                icon = icon,
                                exTitle = title,
                                exDescription = description
                            )
                        )
                    },
                )
            }
        }
    }

    fun updateExerciseItem(updatedExerciseItem: ExerciseItem) {
        viewModelScope.launch {
            dsModule.exercisesDS.updateData { currentList ->
                currentList.copy(
                    excList = currentList.excList.map { item ->
                        if (item.id == updatedExerciseItem.id) updatedExerciseItem else item
                    }.toPersistentList()
                )
            }
        }
    }

    fun deleteExerciseItem(itemId: String) {
        viewModelScope.launch {
            dsModule.exercisesDS.updateData { it ->
                it.copy(
                    excList = it.excList.filterNot { it.id == itemId }.toPersistentList()
                )
            }
        }
    }
}
