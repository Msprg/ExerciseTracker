package com.msprg.exerciseTracker.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Square
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RowItem(
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Outlined.Square,
    title: String = "RowItemTitle",
    description: String = "RowItemDescription",
    onClick: () -> Unit = { },
    onLongClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier.combinedClickable(
            onClick = onClick, onLongClick = onLongClick
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon, contentDescription = null,
            modifier = Modifier.size(55.dp)
        )
        Column(
            modifier = modifier.fillMaxWidth()
        ) {
            Text(text = title, fontSize = 20.sp)
            Text(text = description, fontSize = 12.sp)
        }
    }
}

@Composable
fun RowItemWithButton(
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Outlined.Square,
    title: String = "RowItemTitle",
    description: String = "RowItemDescription",
    onClick: () -> Unit = { }
) {
    Row(
        modifier = Modifier.clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon, contentDescription = null,
            modifier = Modifier.size(55.dp)
        )
        Column(horizontalAlignment = Alignment.Start) {
            Text(text = title, fontSize = 20.sp)
            Text(text = description, fontSize = 12.sp)
        }
        Spacer(modifier = modifier.weight(1f))

        IconButton(onClick = { /*TODO*/ }, content = {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                tint = Color.Green,
                contentDescription = null,
                modifier = Modifier.size(55.dp)
            )
        })

    }
}

@Composable
fun RowItemsMockup(
    modifier: Modifier = Modifier, howMany: Int = 50,
    icon: ImageVector = Icons.Outlined.Square,
    title: String = "MOCK_ITEM_TITLE",
    description: String = "MOCK_ITEM_DESCRIPTION"
) {
    LazyColumn {
        items(howMany) { index ->
            RowItem(
                icon = icon,
                title = "$title $index", description = "$description $index"
            )
        }
    }
}

@Composable
fun RowItemsMockupWithButton(
    modifier: Modifier = Modifier, howMany: Int = 50,
    icon: ImageVector = Icons.Outlined.Square,
    title: String = "MOCK_ITEM_TITLE",
    description: String = "MOCK_ITEM_DESCRIPTION"
) {
    LazyColumn {
        items(howMany) { index ->
            RowItemWithButton(
                icon = icon,
                title = "$title $index", description = "$description $index"
            )
        }
    }
}
