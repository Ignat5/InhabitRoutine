package com.example.inhabitroutine.feature.view_schedule.config.view_task_actions

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.inhabitroutine.core.presentation.R
import com.example.inhabitroutine.core.presentation.ui.base.BaseDialogWithResult
import com.example.inhabitroutine.core.presentation.ui.dialog.base.BaseModalBottomSheetDialog
import com.example.inhabitroutine.core.presentation.ui.util.limitNumberToDisplay
import com.example.inhabitroutine.core.presentation.ui.util.toDayMonthYearDisplay
import com.example.inhabitroutine.core.presentation.ui.util.toDisplay
import com.example.inhabitroutine.core.presentation.ui.util.toHourMinute
import com.example.inhabitroutine.domain.model.record.content.RecordEntry
import com.example.inhabitroutine.domain.model.task.content.TaskProgress
import com.example.inhabitroutine.feature.view_schedule.config.view_task_actions.components.ViewTaskActionsScreenEvent
import com.example.inhabitroutine.feature.view_schedule.config.view_task_actions.components.ViewTaskActionsScreenResult
import com.example.inhabitroutine.feature.view_schedule.config.view_task_actions.components.ViewTaskActionsScreenState
import com.example.inhabitroutine.feature.view_schedule.config.view_task_actions.model.ItemTaskAction
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

@Composable
internal fun ViewTaskActionsDialog(
    stateHolder: ViewTaskActionsStateHolder,
    onResult: (ViewTaskActionsScreenResult) -> Unit
) {
    BaseDialogWithResult(stateHolder = stateHolder, onResult = onResult) { state, onEvent ->
        ViewTaskActionsDialogStateless(state, onEvent)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ViewTaskActionsDialogStateless(
    state: ViewTaskActionsScreenState,
    onEvent: (ViewTaskActionsScreenEvent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    BaseModalBottomSheetDialog(
        sheetState = sheetState,
        dragHandle = null,
        onDismissRequest = { onEvent(ViewTaskActionsScreenEvent.OnDismissRequest) }) {
        Column(modifier = Modifier.fillMaxWidth()) {
            TaskTitleRow(
                taskTitle = state.task.title,
                date = state.date,
                onEditClick = { onEvent(ViewTaskActionsScreenEvent.OnEditTaskClick) },
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(
                    items = state.allTaskActionItems,
                    key = { it.type }
                ) { item ->
                    val onItemClick: () -> Unit = remember {
                        val callback: () -> Unit = {
                            onEvent(ViewTaskActionsScreenEvent.OnItemActionClick(item.type))
//                            scope.launch {
//                                sheetState.hide()
//                                onEvent(ViewTaskActionsScreenEvent.OnItemActionClick(item.type))
//                            }
                        }
                        callback
                    }
                    when (item.type) {
                        ItemTaskAction.Type.Progress -> {
                            if (item is ItemTaskAction.ContinuousProgress.Number) {
                                ItemProgressNumber(
                                    progress = item.progress,
                                    entry = item.entry,
                                    onClick = onItemClick
                                )
                            } else if (item is ItemTaskAction.ContinuousProgress.Time) {
                                ItemProgressTime(
                                    progress = item.progress,
                                    entry = item.entry,
                                    onClick = onItemClick
                                )
                            }
                        }

                        ItemTaskAction.Type.Done -> {
                            ItemDone(onItemClick)
                        }

                        ItemTaskAction.Type.Skip -> {
                            ItemSkip(onItemClick)
                        }

                        ItemTaskAction.Type.Fail -> {
                            ItemFail(onItemClick)
                        }

                        ItemTaskAction.Type.Reset -> {
                            ItemReset(onItemClick)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TaskTitleRow(
    taskTitle: String,
    date: LocalDate,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            val dateText = remember {
                date.toDayMonthYearDisplay()
            }
            Text(
                text = taskTitle,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = dateText,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        IconButton(onClick = onEditClick) {
            Icon(
                painter = painterResource(id = R.drawable.ic_edit),
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun ItemProgressNumber(
    progress: TaskProgress.Number,
    entry: RecordEntry.HabitEntry.Continuous.Number?,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val title = remember {
        val entryNumber = (entry as? RecordEntry.Number)?.number ?: 0.0
        "${entryNumber.limitNumberToDisplay()} ${progress.limitUnit}"
    }
    val subtitle = remember {
        buildString {
            append(context.getString(R.string.goal))
            append(": ")
            append(progress.toDisplay(context))
        }
    }
    BaseTitleSubtitleItem(
        iconResId = R.drawable.ic_goal,
        title = title,
        subtitle = subtitle,
        onClick = onClick
    )
}

@Composable
private fun ItemProgressTime(
    progress: TaskProgress.Time,
    entry: RecordEntry.HabitEntry.Continuous.Time?,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val title = remember {
        val entryTime = (entry as? RecordEntry.Time)?.time ?: LocalTime(hour = 0, minute = 0)
        entryTime.toHourMinute()
    }
    val subtitle = remember {
        buildString {
            append(context.getString(R.string.goal))
            append(": ")
            append(progress.toDisplay(context))
        }
    }
    BaseTitleSubtitleItem(
        iconResId = R.drawable.ic_goal,
        title = title,
        subtitle = subtitle,
        onClick = onClick
    )
}

@Composable
private fun ItemDone(onClick: () -> Unit) {
    BaseTitleItem(
        iconResId = R.drawable.ic_done,
        stringResId = R.string.task_action_done,
        onClick = onClick
    )
}

@Composable
private fun ItemSkip(onClick: () -> Unit) {
    BaseTitleItem(
        iconResId = R.drawable.ic_skip,
        stringResId = R.string.task_action_skip,
        onClick = onClick
    )
}

@Composable
private fun ItemFail(onClick: () -> Unit) {
    BaseTitleItem(
        iconResId = R.drawable.ic_failure,
        stringResId = R.string.task_action_fail,
        onClick = onClick
    )
}

@Composable
private fun ItemReset(onClick: () -> Unit) {
    BaseTitleItem(
        iconResId = R.drawable.ic_reset,
        stringResId = R.string.task_action_reset_entry,
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
private fun BaseTitleSubtitleItem(
    @DrawableRes iconResId: Int,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    BaseItemContainer(
        iconResId = iconResId,
        onClick = onClick,
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        }
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

