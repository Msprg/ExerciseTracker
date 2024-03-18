package com.msprg.exerciseTracker.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Square
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class RowItem {
}

@Composable
fun RowItem(
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Outlined.Square,
    title: String = "RowItemTitle",
    description: String = "RowItemDescription",
    onClick: () -> Unit = { }
) {
    Row(
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Icon(
            imageVector = icon, contentDescription = null,
            modifier = Modifier.size(55.dp)
        )
        Column(modifier = modifier.fillMaxSize()) {
            Text(text = title, fontSize = 20.sp)
            Text(text = description, fontSize = 12.sp)
        }
    }
}

@Composable
fun RowItemsMockup(
    modifier: Modifier = Modifier, howMany: Int = 50,
    icon: ImageVector = Icons.Outlined.Square,
    title: String = "MOCK_ITEM_TITLE",
    description: String = "MOCK_ITEM_DESCRIPTION"
) {
    LazyColumn() {
        items(howMany) { index ->
            RowItem(
                icon = icon,
                title = "$title $index", description = "$description $index"
            )
        }
    }
}