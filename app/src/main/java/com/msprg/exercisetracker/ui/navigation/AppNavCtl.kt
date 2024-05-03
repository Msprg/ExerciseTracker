package com.msprg.exerciseTracker.ui.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.msprg.exerciseTracker.ExTrApplication
import com.msprg.exerciseTracker.data.ExercisesList
import com.msprg.exerciseTracker.data.RoutinesList
import com.msprg.exerciseTracker.ui.screens.ExerciseItemEditScreen
import com.msprg.exerciseTracker.ui.screens.ExerciseItemViewScreen
import com.msprg.exerciseTracker.ui.screens.ExercisesScreen
import com.msprg.exerciseTracker.ui.screens.HistoryScreen
import com.msprg.exerciseTracker.ui.screens.PlayRoutineScreen
import com.msprg.exerciseTracker.ui.screens.RoutineItemEditScreen
import com.msprg.exerciseTracker.ui.screens.RoutinesScreen
import com.msprg.exerciseTracker.ui.screens.ScheduleScreen
import com.msprg.exerciseTracker.ui.viewmodels.ExercisesViewModel
import com.msprg.exerciseTracker.ui.viewmodels.RoutinesViewModel

@Composable
fun AppNavCtl(
    startingScreen: Screens = Screens.HistoryScreen,
    viewModel: ExercisesViewModel = ExercisesViewModel(ExTrApplication.datastoremodule),
    routinesViewModel: RoutinesViewModel = RoutinesViewModel(ExTrApplication.datastoremodule)
) {
    val navController = rememberNavController()
    val exerciseData by viewModel.exerciseDataFlow.collectAsState(initial = ExercisesList())
    val routinesData by routinesViewModel.routinesDataFlow.collectAsState(initial = RoutinesList())

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                NavItemsList.forEach { navItem ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.route == navItem.route } == true,
                        onClick = {
                            navController.navigate(navItem.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            if (currentDestination != null) {
                                Icon(
                                    imageVector = if (currentDestination.route == navItem.route)
                                        navItem.selectedIcon else navItem.unselectedIcon,
                                    contentDescription = null
                                )
                            }
                        },
                        label = { Text(text = navItem.label) }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = startingScreen.name,
            modifier = Modifier
                .padding(paddingValues)
        ) {
            composable(route = Screens.ExercisesScreen.name) {
                ExercisesScreen(
                    navCtl = navController
                )
            }
            composable(
                route = "${Screens.ExerciseItemViewScreen.name}/{exerciseItemUUID}",
                arguments = listOf(navArgument("exerciseItemUUID") { type = NavType.StringType })
            ) { backStackEntry ->
                val exerciseItemUUID = backStackEntry.arguments?.getString("exerciseItemUUID") ?: ""
                val exerciseItem = exerciseData.excList.find { it.id == exerciseItemUUID }
                if (exerciseItem != null) {
                    ExerciseItemViewScreen(
                        exerciseItem = exerciseItem,
                        onBackPressed = { navController.popBackStack() },
                        onEditPressed = {
                            navController.navigate("${Screens.ExerciseItemEditScreen.name}/$exerciseItemUUID")
                        }
                    )
                } else {
                    Toast.makeText(
                        ExTrApplication.appContext,
                        "No such item found :(",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Navigate back to the previous screen
                    navController.popBackStack()
                }
            }
            composable(
                route = "${Screens.ExerciseItemEditScreen.name}/{exerciseItemUUID}",
                arguments = listOf(navArgument("exerciseItemUUID") { type = NavType.StringType })
            ) { backStackEntry ->
                val exerciseItemUUID = backStackEntry.arguments?.getString("exerciseItemUUID") ?: ""
                val exerciseItem = exerciseData.excList.find { it.id == exerciseItemUUID }
                ExerciseItemEditScreen(
                    exerciseItem = exerciseItem,
                    onBackPressed = { navController.popBackStack() },
                    onSavePressed = { updatedExerciseItem ->
                        viewModel.updateExerciseItem(updatedExerciseItem)
                        navController.popBackStack()
                    }
                )
            }
            composable(
                route = Screens.ExerciseItemEditScreen.name
            ) {
                ExerciseItemEditScreen(
                    exerciseItem = null,
                    onBackPressed = { navController.popBackStack() },
                    onSavePressed = { newExerciseItem ->
                        viewModel.addExerciseItem(
                            icon = newExerciseItem.icon,
                            newExerciseItem.exTitle,
                            newExerciseItem.exDescription
                        )
                        navController.popBackStack()
                    }
                )
            }
            composable(route = Screens.RoutinesScreen.name) {
                RoutinesScreen(navCtl = navController)
            }
            composable(
                route = "${Screens.RoutineItemEditScreen.name}/{routineItemId}",
                arguments = listOf(navArgument("routineItemId") { type = NavType.StringType })
            ) { backStackEntry ->
                val routineItemId = backStackEntry.arguments?.getString("routineItemId")
                val routineItem = routinesData.routineList.find { it.id == routineItemId }
                RoutineItemEditScreen(
                    routineItem = routineItem,
                    onBackPressed = { navController.popBackStack() },
                    onSavePressed = { updatedRoutineItem ->
                        routinesViewModel.updateRoutine(updatedRoutineItem)
                        navController.popBackStack()
                    }
                )
            }

            composable(route = Screens.RoutineItemEditScreen.name) {
                RoutineItemEditScreen(
                    routineItem = null,
                    onBackPressed = { navController.popBackStack() },
                    onSavePressed = { newRoutineItem ->
                        routinesViewModel.addRoutine(newRoutineItem)
                        navController.popBackStack()
                    }
                )
            }
            composable(
                route = "${Screens.PlayRoutineScreen.name}/{routineItemId}",
                arguments = listOf(navArgument("routineItemId") { type = NavType.StringType })
            ) { backStackEntry ->
                val routineItemId = backStackEntry.arguments?.getString("routineItemId")
                val routineItem = routinesData.routineList.find { it.id == routineItemId }
                if (routineItem != null) {
                    PlayRoutineScreen(
                        routineItem = routineItem,
                        exerciseList = routineItem.exerciseList,
                        onRoutineFinished = { navController.popBackStack() }
                    )
                }
            }

            composable(route = Screens.ScheduleScreen.name) {
                ScheduleScreen(navCtl = navController)
            }
            composable(route = Screens.HistoryScreen.name) {
                HistoryScreen(navCtl = navController)
            }
        }

    }
}
