package com.msprg.exerciseTracker.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

//kód podľa https://github.com/philipplackner/ProtoDataStoreGuide
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SwipeToDismissContainer(
    item: T,
    dismissDirections: Set<DismissDirection>,
    dismissToStartAction: (T) -> Unit,
    dismissToEndAction: (T) -> Unit,
    animationDuration: Int = 500,
    content: @Composable (T) -> Unit
) {
    var isDismissedToStart by remember { mutableStateOf(false) }
    var isDismissedToEnd by remember { mutableStateOf(false) }
    val state = rememberDismissState(
        confirmValueChange = { value ->
            when (value) {
                DismissValue.DismissedToStart -> {
                    isDismissedToStart = true
                    true
                }

                DismissValue.DismissedToEnd -> {
                    isDismissedToEnd = true
                    true
                }

                else -> false
            }
        }
    )

    LaunchedEffect(key1 = isDismissedToStart) {
        if (isDismissedToStart) {
            delay(animationDuration.toLong() - 250) //shit's bugged https://issuetracker.google.com/issues/240599812
            dismissToStartAction(item)
        }
    }

    LaunchedEffect(key1 = isDismissedToEnd) {
        if (isDismissedToEnd) {
            delay(animationDuration.toLong() - 250)
            dismissToEndAction(item)
        }
    }

    AnimatedVisibility(
        visible = !isDismissedToStart && !isDismissedToEnd,
        enter = expandVertically(
            animationSpec = tween(durationMillis = animationDuration),
            expandFrom = Alignment.Top
        ),
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = animationDuration),
            shrinkTowards = Alignment.Top
        ) + fadeOut()
//
//        exit = shrinkHorizontally(
//            animationSpec = tween(durationMillis = animationDuration),
//            shrinkTowards = Alignment.Start
//        ) + fadeOut()

//        exit = shrinkVertically(
//            animationSpec = tween(durationMillis = animationDuration),
//            shrinkTowards = Alignment.Top
//        ) + fadeOut()

    ) {
        SwipeToDismiss(
            state = state,
            background = {
                DismissBackground(swipeDismissState = state)
            },
            dismissContent = { content(item) },
            directions = dismissDirections
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissBackground(
    swipeDismissState: DismissState
) {
    val color = if (swipeDismissState.dismissDirection == DismissDirection.EndToStart) {
        Color.Red
    } else Color.Transparent
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(16.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            tint = Color.White
        )
    }
}