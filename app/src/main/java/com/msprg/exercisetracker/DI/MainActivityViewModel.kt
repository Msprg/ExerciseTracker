package com.msprg.exerciseTracker.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msprg.exerciseTracker.data.ExerciseIcon
import com.msprg.exerciseTracker.data.ExerciseItem
import com.msprg.exerciseTracker.data.ExercisesList
import kotlinx.collections.immutable.mutate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val dsModule: IFDataStoreModule
) : ViewModel() {
    val exerciseDataFlow: Flow<ExercisesList> = dsModule.protoDS.data
    fun addExerciseItem(
        icon: ExerciseIcon,
        title: String,
        description: String
    ) {
        viewModelScope.launch {
            dsModule.protoDS.updateData {
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

    fun deleteExerciseItem(index: Int) {
        viewModelScope.launch {
            dsModule.protoDS.updateData { it ->
                it.copy(
                    excList = it.excList.mutate {
                        it.removeAt(index)
                    }
                )
            }
        }
    }
}