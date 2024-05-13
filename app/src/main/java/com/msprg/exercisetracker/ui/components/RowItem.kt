package com.msprg.exerciseTracker.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.msprg.exerciseTracker.R

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RowItem(
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = {
        Icon(
            imageVector = Icons.Outlined.Square,
            contentDescription = null,
            modifier = Modifier
                .size(55.dp)
                .padding(start = 8.dp)
        )
    },
    title: String = stringResource(R.string.rowitemtitle),
    description: String = stringResource(R.string.rowitemdescription),
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    onDismissToStart: (() -> Unit)? = null,
    onDismissToEnd: (() -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null
) {
    // Apply appropriate dismiss directions based on onDismissToStart and onDismissToEnd
    val dismissDirections = mutableSetOf<DismissDirection>().apply {
        if (onDismissToStart != null) {
            add(DismissDirection.EndToStart)
        }
        if (onDismissToEnd != null) {
            add(DismissDirection.StartToEnd)
        }
    }

    SwipeToDismissContainer(
        item = Unit,
        dismissDirections = dismissDirections,
        dismissToStartAction = { onDismissToStart?.invoke() },
        dismissToEndAction = { onDismissToEnd?.invoke() }
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .combinedClickable(onClick = onClick, onLongClick = onLongClick),
            verticalAlignment = Alignment.CenterVertically
        ) {
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
}

@Composable
fun RowItemsMockup(
    modifier: Modifier = Modifier,
    howMany: Int = 50,
    icon: @Composable (() -> Unit)? = {
        Icon(
            imageVector = Icons.Outlined.Square,
            contentDescription = null,
            modifier = Modifier
                .size(55.dp)
                .padding(start = 8.dp)
        )
    },
    title: String = stringResource(R.string.mock_item_title),
    description: String = stringResource(R.string.mock_item_description),
    trailingContent: @Composable (() -> Unit)? = null
) {
    LazyColumn(modifier = modifier) {
        items(howMany) { index ->
            RowItem(
                icon = icon,
                title = "$title $index",
                description = "$description $index",
                trailingContent = trailingContent
            )
        }
    }
}

