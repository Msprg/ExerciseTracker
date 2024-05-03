package com.msprg.exerciseTracker.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msprg.exerciseTracker.data.HistoryItem
import com.msprg.exerciseTracker.di.IFDataStoreModule
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class HistoryViewModel(
    private val dsModule: IFDataStoreModule
) : ViewModel() {
    private val _historyItems = MutableStateFlow<List<HistoryItem>>(emptyList())
    val historyItems: StateFlow<List<HistoryItem>> = _historyItems.asStateFlow()

    init {
        // Load history items from the data store
        viewModelScope.launch {
            dsModule.historyDS.data.collect { items ->
                _historyItems.value = items.historyList
            }
        }
    }

    fun addHistoryItem(
        routineId: String,
        routineTitle: String,
        startTime: Long,
        endTime: Long,
        completed: Boolean
    ): String {
        val historyItem = HistoryItem(
            routineId = routineId,
            routineTitle = routineTitle,
            startTime = startTime,
            endTime = endTime,
            completed = completed
        )
        viewModelScope.launch {
            dsModule.historyDS.updateData { currentList ->
                currentList.copy(
                    historyList = currentList.historyList.add(historyItem)
                )
            }
        }
        return historyItem.id
    }

    fun updateHistoryItem(id: String, endTime: Long, completed: Boolean) {
        viewModelScope.launch {
            dsModule.historyDS.updateData { currentList ->
                currentList.copy(
                    historyList = currentList.historyList.map { item ->
                        if (item.id == id) {
                            item.copy(endTime = endTime, completed = completed)
                        } else item
                    }.toPersistentList()
                )
            }
        }
    }

    fun getHistoryItemsForDate(date: Long): List<HistoryItem> {
        val selectedDate = Date(date)
        val startOfDay = Calendar.getInstance().apply {
            time = selectedDate
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
        val endOfDay = Calendar.getInstance().apply {
            time = selectedDate
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }.timeInMillis

        return _historyItems.value.filter { item ->
            item.startTime in startOfDay..endOfDay
        }
    }

}
