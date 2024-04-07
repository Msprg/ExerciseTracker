package com.msprg.exerciseTracker.ui.navigation

import android.content.res.Configuration
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.msprg.exerciseTracker.ExTrApplication
import com.msprg.exerciseTracker.MainActivityViewModel
import com.msprg.exerciseTracker.data.ExercisesList
import com.msprg.exerciseTracker.ui.screens.ExerciseItemViewScreen
import com.msprg.exerciseTracker.ui.screens.ExercisesScreen
import com.msprg.exerciseTracker.ui.screens.HistoryScreen
import com.msprg.exerciseTracker.ui.screens.RoutinesScreen
import com.msprg.exerciseTracker.ui.screens.ScheduleScreen
import com.msprg.exerciseTracker.ui.theme.ExerciseTrackerTheme

@Composable
fun AppNavCtl(
    startingScreen: Screens = Screens.ExercisesScreen,
    viewModel: MainActivityViewModel = MainActivityViewModel(ExTrApplication.datastoremodule)
) {
    val navController = rememberNavController()
    val exerciseData by viewModel.exerciseDataFlow.collectAsState(initial = ExercisesList())
    ExTrApplication

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
                route = "${Screens.ExerciseItemViewScreen.name}/{exerciseItemIndex}",
                arguments = listOf(navArgument("exerciseItemIndex") { type = NavType.IntType })
            ) {
                val exerciseItemIndex = it.arguments?.getInt("exerciseItemIndex") ?: 0
                val exerciseItem =
                    exerciseData.excList[exerciseItemIndex] //java.lang.IndexOutOfBoundsException: index: 0, size: 0
                ExerciseItemViewScreen(
                    exerciseItem = exerciseItem,
                    onBackPressed = { navController.popBackStack() }
                )
            }
            composable(route = Screens.RoutinesScreen.name) {
                RoutinesScreen()
            }
            composable(route = Screens.ScheduleScreen.name) {
                ScheduleScreen()
            }
            composable(route = Screens.HistoryScreen.name) {
                HistoryScreen()
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AppNavCtlPrew() {
    ExerciseTrackerTheme {
        AppNavCtl()
    }
}
