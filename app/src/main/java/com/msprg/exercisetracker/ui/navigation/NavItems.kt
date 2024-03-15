package com.msprg.exerciseTracker.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.EventNote
import androidx.compose.material.icons.automirrored.rounded.EventNote
import androidx.compose.material.icons.outlined.EventAvailable
import androidx.compose.material.icons.outlined.EventRepeat
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.rounded.EventAvailable
import androidx.compose.material.icons.rounded.EventRepeat
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.ui.graphics.vector.ImageVector

data class NavItem(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String
)

val NavItemsList = listOf(
    NavItem(
        label = "Exercises",
        selectedIcon = Icons.Rounded.FitnessCenter,
        unselectedIcon = Icons.Outlined.FitnessCenter,
        route = Screens.ExercisesScreen.name
    ),
    NavItem(
        label = "Routines",
        selectedIcon = Icons.Rounded.EventRepeat,
        unselectedIcon = Icons.Outlined.EventRepeat,
        route = Screens.RoutinesScreen.name
    ),
    NavItem(
        label = "Schedule",
        selectedIcon = Icons.Rounded.EventAvailable,
        unselectedIcon = Icons.Outlined.EventAvailable,
        route = Screens.ScheduleScreen.name
    ),
    NavItem(
        label = "History",
        selectedIcon = Icons.AutoMirrored.Rounded.EventNote,
        unselectedIcon = Icons.AutoMirrored.Outlined.EventNote,
        route = Screens.HistoryScreen.name
    ),
)
