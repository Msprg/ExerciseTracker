package com.msprg.exerciseTracker.ui.screens

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.msprg.exerciseTracker.DataStoreRepo
import com.msprg.exerciseTracker.ExerciseItemData
import com.msprg.exerciseTracker.PersistUserdataSerializer
import com.msprg.exerciseTracker.ui.components.RowItemsMockup
import com.msprg.exerciseTracker.ui.navigation.AppNavCtl
import com.msprg.exerciseTracker.ui.navigation.Screens
import com.msprg.exerciseTracker.ui.theme.ExerciseTrackerTheme
import kotlinx.coroutines.launch
import kotlin.properties.ReadOnlyProperty

val datastore: ReadOnlyProperty<Context, DataStore<ExerciseItemData>> = dataStore("Userdata.json", PersistUserdataSerializer)
@Composable
fun ExercisesScreen(dataStore: DataStore<ExerciseItemData>) {
    var useless by remember {
        mutableIntStateOf(0)
    }
	val userData = dataStore.data.collectAsState(initial = ExerciseItemData()).value
	val scope = rememberCoroutineScope()
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    useless++
					scope.launch {
						dataStore.updateData {
							it.copy(
								exTitle = "Exercise Title number $useless",
								exDescription = "Exercise Description $useless"
							)
						}
					}
                },
                shape = CircleShape
            ) {
                Icon(Icons.Rounded.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
				.padding(innerPadding)
				.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
        ) {
            RowItemsMockup(icon = Icons.Default.FitnessCenter,
				title = userData.exTitle,
				description = userData.exDescription
			)
        }
    }
}
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ExercisesScreenPrew() {
    ExerciseTrackerTheme {
//        AppNavCtl(Screens.ExercisesScreen)
    }
}
