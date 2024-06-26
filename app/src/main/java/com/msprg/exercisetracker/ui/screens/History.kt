package com.msprg.exerciseTracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.msprg.exerciseTracker.ExTrApplication
import com.msprg.exerciseTracker.R
import com.msprg.exerciseTracker.ui.components.RowItem
import com.msprg.exerciseTracker.ui.viewmodels.HistoryViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    navCtl: NavController,
    viewModel: HistoryViewModel = HistoryViewModel(ExTrApplication.datastoremodule)
) {
    val historyItems by viewModel.historyItems.collectAsState()

    // Get the current date
    val currentDate = remember {
        Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            add(Calendar.DATE, 1) // For some reason it's giving me the previous day
        }.timeInMillis
    }

    // Initialize the date picker state with the current date
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = currentDate)
    val selectedDate = datePickerState.selectedDateMillis ?: currentDate

    var filteredHistoryItems = viewModel.getHistoryItemsForDate(selectedDate)
    LaunchedEffect(historyItems) {
        if (historyItems.isNotEmpty()) {
            filteredHistoryItems = viewModel.getHistoryItemsForDate(selectedDate)
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize(),
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                DatePicker(
                    state = datePickerState,
                    modifier = Modifier.padding(16.dp),
                    title = null,
                    headline = null,
                    showModeToggle = false
                )
                Text(
                    stringResource(
                        R.string.selected_date,
                        SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(
                            selectedDate
                        )
                    ),
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .align(Alignment.Start),
                    fontSize = 18.sp
                )
                Box(modifier = Modifier.weight(1f)) {
                    LazyColumn {
                        items(filteredHistoryItems, key = { it.id }) { historyItem ->
                            val localizedStartTime = SimpleDateFormat(
                                "HH:mm",
                                Locale.getDefault()
                            ).format(historyItem.startTime)
                            val localizedDate =
                                SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(
                                    historyItem.startTime
                                )
                            val durationText = if (historyItem.completed) {
                                val duration = historyItem.endTime - historyItem.startTime
                                val minutes = duration / 60000
                                val seconds = (duration % 60000) / 1000
                                ExTrApplication.appContext.getString(
                                    R.string.duration_m_s,
                                    minutes,
                                    seconds
                                )
                            } else {
                                ExTrApplication.appContext.getString(R.string.routine_not_finished)
                            }

                            RowItem(
                                icon = {
                                    Icon(
                                        imageVector = Icons.Outlined.History,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(55.dp)
                                            .padding(start = 8.dp)
                                    )
                                },
                                title = historyItem.routineTitle,
                                description = ExTrApplication.appContext.getString(
                                    R.string.start,
                                    localizedStartTime,
                                    localizedDate,
                                    durationText
                                ),
                            )
                        }
                    }
                }
            }
        }
    )
}


