package com.msprg.exerciseTracker.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Square
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RowItem(
    modifier: Modifier = Modifier,
//    icon: ImageVector = Icons.Outlined.Square,
    icon: (@Composable () -> Unit)? = {
        Icon(
            imageVector = Icons.Outlined.Square,
            contentDescription = null,
            modifier = Modifier
                .size(55.dp)
                .padding(start = 8.dp)
        )
    },
    title: String = "RowItemTitle",
    description: String = "RowItemDescription",
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    trailingContent: (@Composable () -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(onClick = onClick, onLongClick = onLongClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
//        Icon(
//            imageVector = icon,
//            contentDescription = null,
//            modifier = Modifier
//                .size(55.dp)
//                .padding(start = 16.dp)
//        )
        icon?.invoke()
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
                .fillMaxHeight()
                .heightIn(min = 80.dp)
        ) {
            Text(text = title, fontSize = 20.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(
                text = description,
                fontSize = 12.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        trailingContent?.invoke()
    }
}

//@Composable
//fun RowItemWithButton(
//    modifier: Modifier = Modifier,
//    icon: ImageVector = Icons.Outlined.Square,
//    title: String = "RowItemTitle",
//    description: String = "RowItemDescription",
//    onClick: () -> Unit = {},
//    onButtonClick: () -> Unit = {}
//) {
//    RowItem(
//        modifier = modifier,
//        icon = icon,
//        title = title,
//        description = description,
//        onClick = onClick,
//        trailingContent = {
//            IconButton(
//                onClick = onButtonClick,
//                modifier = Modifier.padding(end = 16.dp)
//            ) {
//                Icon(
//                    imageVector = Icons.Filled.PlayArrow,
//                    tint = Color.Green,
//                    contentDescription = null,
//                    modifier = Modifier.size(55.dp)
//                )
//            }
//        }
//    )
//}

@Composable
fun RowItemsMockup(
    modifier: Modifier = Modifier,
    howMany: Int = 50,
//    icon: ImageVector = Icons.Outlined.Square,
    icon: (@Composable () -> Unit)? = {
        Icon(
            imageVector = Icons.Outlined.Square,
            contentDescription = null,
            modifier = Modifier
                .size(55.dp)
                .padding(start = 8.dp)
        )
    },
    title: String = "MOCK_ITEM_TITLE",
    description: String = "MOCK_ITEM_DESCRIPTION",
    trailingContent: (@Composable () -> Unit)? = null,
    onClick: () -> Unit = {},
    onButtonClick: () -> Unit = {}
) {
    LazyColumn(modifier = modifier) {
        items(howMany) { index ->
            RowItem(
                icon = icon,
                title = "$title $index AAAAAA",
                description = "$description $index",
                trailingContent = trailingContent
            )
        }
    }
}

//@Composable
//fun RowItemsMockupWithButton(
//    modifier: Modifier = Modifier,
//    howMany: Int = 50,
//    icon: ImageVector = Icons.Outlined.Square,
//    title: String = "MOCK_ITEM_TITLE",
//    description: String = "MOCK_ITEM_DESCRIPTION"
//) {
//    LazyColumn(modifier = modifier) {
//        items(howMany) { index ->
//            RowItemWithButton(
//                icon = icon,
//                title = "$title $index",
//                description = "$description $index"
//            )
//        }
//    }
//}
