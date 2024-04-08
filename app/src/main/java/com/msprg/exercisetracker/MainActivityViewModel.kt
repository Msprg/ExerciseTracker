package com.msprg.exerciseTracker

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

    fun deleteExerciseItem(itemId: String) {
        viewModelScope.launch {
            dsModule.protoDS.updateData { it ->
                it.copy(
                    excList = it.excList.filterNot { it.id == itemId }.toPersistentList()
                )
            }
        }
    }
}
