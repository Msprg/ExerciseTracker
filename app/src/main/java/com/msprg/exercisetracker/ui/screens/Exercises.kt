package com.msprg.exerciseTracker.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.msprg.exerciseTracker.ui.navigation.AppNavCtl
import com.msprg.exerciseTracker.ui.navigation.Screens
import com.msprg.exerciseTracker.ui.theme.ExerciseTrackerTheme

@Composable
fun ExercisesScreen() {
    var useless by remember {
        mutableIntStateOf(0)
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { useless++ },
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
//            Row {
//                Icon(
//                    imageVector = Icons.Default.FitnessCenter, contentDescription = null,
//                    modifier = Modifier.size(50.dp)
//                )
//                Column {
//                    Text(text = "ItemTitle", fontSize = 20.sp)
//                    Text(text = "ItemDescription", fontSize = 15.sp)
//                }
//            }
            ItemsList()
            Text(text = "EX_CONTENT $useless", fontSize = 32.sp)
        }
//        Box(
//            modifier = Modifier
//                .padding(innerPadding)
//                .fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) {
//            Text(text = "EX_CONTENT $useless", fontSize = 32.sp)
//        }
    }
}

@Composable
fun AnItem(
    modifier: Modifier = Modifier,
    number: Int = 0,
    title: String = "ItemTitle",
    description: String = "ItemDescription"
) {
    Row(
        modifier = Modifier.clickable { }
    ) {
        Icon(
            imageVector = Icons.Default.FitnessCenter, contentDescription = null,
            modifier = Modifier.size(55.dp)
        )
        Column(modifier = modifier.fillMaxSize()) {
            Text(text = title + " " + number, fontSize = 20.sp)
            Text(text = description, fontSize = 12.sp)
        }
    }
}

@Composable
fun ItemsList(modifier: Modifier = Modifier) {
    LazyColumn() {
        items(50) { index ->
            AnItem(
                number = index,
                title = "MOCK_ITEM_TITLE", description = "MOCK_ITEM_DESCRIPTION"
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ExercisesScreenPrew() {
    ExerciseTrackerTheme {
        AppNavCtl(Screens.ExercisesScreen)
    }
}
