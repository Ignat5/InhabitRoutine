package com.example.inhabitroutine.feature.view_habits.config.view_habit_actions

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.inhabitroutine.core.presentation.R
import com.example.inhabitroutine.core.presentation.ui.base.BaseDialogWithResult
import com.example.inhabitroutine.core.presentation.ui.dialog.base.BaseModalBottomSheetDialog
import com.example.inhabitroutine.feature.view_habits.config.view_habit_actions.components.ViewHabitActionsScreenEvent
import com.example.inhabitroutine.feature.view_habits.config.view_habit_actions.components.ViewHabitActionsScreenResult
import com.example.inhabitroutine.feature.view_habits.config.view_habit_actions.components.ViewHabitActionsScreenState
import com.example.inhabitroutine.feature.view_habits.config.view_habit_actions.model.ItemHabitAction

@Composable
fun ViewHabitActionsDialog(
    stateHolder: ViewHabitActionsStateHolder,
    onResult: (ViewHabitActionsScreenResult) -> Unit
) {
    BaseDialogWithResult(stateHolder = stateHolder, onResult = onResult) { state, onEvent ->
        ViewHabitActionsDialogStateless(state, onEvent)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ViewHabitActionsDialogStateless(
    state: ViewHabitActionsScreenState,
    onEvent: (ViewHabitActionsScreenEvent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    BaseModalBottomSheetDialog(
        sheetState = sheetState,
        dragHandle = null,
        onDismissRequest = { onEvent(ViewHabitActionsScreenEvent.OnDismissRequest) }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            TaskTitleRow(
                title = state.taskModel.title,
                onEditClick = {
                    onEvent(ViewHabitActionsScreenEvent.OnEditClick)
                },
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
            )
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(state.allHabitActionItems) { item ->
                    val onClick: () -> Unit = remember {
                        val callback: () -> Unit = {
                            onEvent(ViewHabitActionsScreenEvent.OnItemActionClick(item))
                        }
                        callback
                    }
                    when (item) {
                        is ItemHabitAction.ViewStatistics -> {
                            ItemViewStatistics(onClick)
                        }
                        is ItemHabitAction.Archive -> {
                            ItemArchive(onClick)
                        }
                        is ItemHabitAction.Unarchive -> {
                            ItemUnarchive(onClick)
                        }
                        is ItemHabitAction.Delete -> {
                            ItemDelete(onClick)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TaskTitleRow(
    title: String,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        IconButton(onClick = onEditClick) {
            Icon(
                painter = painterResource(id = R.drawable.ic_edit),
                contentDescription = null
            )
        }
    }
}

@Composable
private fun ItemViewStatistics(onClick: () -> Unit) {
    BaseTitleItem(
        iconResId = R.drawable.ic_statistics,
        stringResId = R.string.task_action_view_statistics,
        onClick = onClick
    )
}

@Composable
private fun ItemArchive(onClick: () -> Unit) {
    BaseTitleItem(
        iconResId = R.drawable.ic_archive,
        stringResId = R.string.task_action_archive,
        onClick = onClick
    )
}

@Composable
private fun ItemUnarchive(onClick: () -> Unit) {
    BaseTitleItem(
        iconResId = R.drawable.ic_unarchive,
        stringResId = R.string.task_action_unarchive,
        onClick = onClick
    )
}

@Composable
private fun ItemDelete(onClick: () -> Unit) {
    BaseTitleItem(
        iconResId = R.drawable.ic_delete,
        stringResId = R.string.task_action_delete,
        onClick = onClick
    )
}

@Composable
private fun BaseTitleItem(
    @DrawableRes iconResId: Int,
    @StringRes stringResId: Int,
    onClick: () -> Unit
) {
    BaseItemContainer(
        iconResId = iconResId,
        onClick = onClick,
    ) {
        Text(
            text = stringResource(id = stringResId),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun BaseItemContainer(
    @DrawableRes iconResId: Int,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = iconResId),
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null
            )
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                content()
            }
        }
    }
}