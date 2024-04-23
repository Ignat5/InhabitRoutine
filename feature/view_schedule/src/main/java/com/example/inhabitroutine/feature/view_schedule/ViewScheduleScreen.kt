package com.example.inhabitroutine.feature.view_schedule

import android.content.Context
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.inhabitroutine.core.presentation.R
import com.example.inhabitroutine.core.presentation.ui.common.ChipTaskProgressType
import com.example.inhabitroutine.core.presentation.ui.common.ChipTaskReminders
import com.example.inhabitroutine.core.presentation.ui.common.ChipTaskType
import com.example.inhabitroutine.core.presentation.ui.common.CreateTaskFAB
import com.example.inhabitroutine.core.presentation.ui.dialog.pick_task_progress_type.PickTaskProgressTypeDialog
import com.example.inhabitroutine.core.presentation.ui.dialog.pick_task_type.PickTaskTypeDialog
import com.example.inhabitroutine.core.presentation.ui.util.limitNumberToDisplay
import com.example.inhabitroutine.core.presentation.ui.util.toHourMinute
import com.example.inhabitroutine.domain.model.derived.TaskStatus
import com.example.inhabitroutine.domain.model.derived.TaskWithExtrasAndRecordModel
import com.example.inhabitroutine.domain.model.record.content.RecordEntry
import com.example.inhabitroutine.feature.view_schedule.components.ViewScheduleScreenConfig
import com.example.inhabitroutine.feature.view_schedule.components.ViewScheduleScreenEvent
import com.example.inhabitroutine.feature.view_schedule.components.ViewScheduleScreenState

private const val PROGRESS_INDICATOR_SIZE_DP = 32
private const val PROGRESS_INDICATOR_STROKE_WIDTH_DP = 2
private const val COMPLETED_PROGRESS = 1f
private const val NOT_COMPLETED_PROGRESS = 0f

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ViewScheduleScreen(
    state: ViewScheduleScreenState,
    onEvent: (ViewScheduleScreenEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            ScreenTopBar(
                onMenuClick = {},
                onSearchClick = {
                    onEvent(ViewScheduleScreenEvent.OnSearchClick)
                },
                onPickDateClick = {}
            )
        },
        floatingActionButton = {
            CreateTaskFAB(
                onClick = {
                    onEvent(ViewScheduleScreenEvent.OnCreateTaskClick)
                }
            )
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            val context = LocalContext.current
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                itemsIndexed(
                    items = state.allTasks,
                    key = { _, item -> item.taskWithExtrasModel.taskModel.id }
                ) { index, item ->
                    Column(modifier = Modifier.fillMaxWidth()) {
                        ItemTask(
                            item = item,
                            context = context,
                            onClick = { /*TODO*/ },
                            onLongClick = { /*TODO*/ },
                            modifier = Modifier.animateItemPlacement()
                        )
                        if (index != state.allTasks.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.alpha(0.2f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ItemTask(
    item: TaskWithExtrasAndRecordModel,
    context: Context,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                TaskTitleRow(item, context)
                TaskDetailRow(item)
            }
            TaskProgressIndicator(item)
        }
    }
}

@Composable
private fun TaskTitleRow(
    item: TaskWithExtrasAndRecordModel,
    context: Context
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = item.taskWithExtrasModel.taskModel.title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        val progressText = remember(item) {
            getProgressTextOrNull(item, context)
        }

        if (progressText != null) {
            Text(
                text = progressText,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun TaskDetailRow(
    item: TaskWithExtrasAndRecordModel
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        ChipTaskType(item.taskWithExtrasModel.taskModel.type)
        ChipTaskProgressType(item.taskWithExtrasModel.taskModel.progressType)
        val allReminders = remember(item.taskWithExtrasModel.taskExtras.allReminders) {
            item.taskWithExtrasModel.taskExtras.allReminders
        }
        if (allReminders.isNotEmpty()) {
            ChipTaskReminders(allReminders = allReminders)
        }
    }
}

private fun getProgressTextOrNull(
    item: TaskWithExtrasAndRecordModel,
    context: Context
): String? {
    return when (item) {
        is TaskWithExtrasAndRecordModel.Habit -> {
            when (item) {
                is TaskWithExtrasAndRecordModel.Habit.HabitContinuous -> {
                    when (item) {
                        is TaskWithExtrasAndRecordModel.Habit.HabitContinuous.HabitNumber -> {
                            when (val entry = item.recordEntry) {
                                null -> null
                                is RecordEntry.Number -> {
                                    item.taskWithExtrasModel.taskModel.progress.let { progress ->
                                        "${entry.number.limitNumberToDisplay()} ${progress.limitUnit}"
                                    }
                                }

                                is RecordEntry.Skip -> context.getString(R.string.task_status_skipped)
                                is RecordEntry.Fail -> context.getString(R.string.task_status_failed)
                            }
                        }

                        is TaskWithExtrasAndRecordModel.Habit.HabitContinuous.HabitTime -> {
                            when (val entry = item.recordEntry) {
                                null -> null
                                is RecordEntry.Time -> {
                                    entry.time.toHourMinute()
                                }

                                is RecordEntry.Skip -> context.getString(R.string.task_status_skipped)
                                is RecordEntry.Fail -> context.getString(R.string.task_status_failed)
                            }
                        }
                    }
                }

                is TaskWithExtrasAndRecordModel.Habit.HabitYesNo -> {
                    when (item.status) {
                        is TaskStatus.Completed -> context.getString(R.string.task_status_done)
                        is TaskStatus.NotCompleted.Pending -> null
                        is TaskStatus.NotCompleted.Skipped -> context.getString(R.string.task_status_skipped)
                        is TaskStatus.NotCompleted.Failed -> context.getString(R.string.task_status_failed)
                    }
                }
            }
        }

        is TaskWithExtrasAndRecordModel.Task -> {
            when (item.status) {
                is TaskStatus.Completed -> context.getString(R.string.task_status_done)
                is TaskStatus.NotCompleted.Pending -> null
            }
        }
    }
}

@Composable
private fun TaskProgressIndicator(
    item: TaskWithExtrasAndRecordModel
) {
    val progress = remember(item) {
        getTaskProgress(item)
    }
    val progressState by animateFloatAsState(
        targetValue = progress,
        animationSpec = spring(stiffness = Spring.StiffnessVeryLow)
    )
    CircularProgressIndicator(
        progress = { progressState },
        modifier = Modifier.size(PROGRESS_INDICATOR_SIZE_DP.dp),
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        trackColor = MaterialTheme.colorScheme.primaryContainer,
        strokeWidth = PROGRESS_INDICATOR_STROKE_WIDTH_DP.dp
    )
}

private fun getTaskProgress(
    item: TaskWithExtrasAndRecordModel
): Float {
    return when (val status = item.status) {
        is TaskStatus.Completed -> COMPLETED_PROGRESS
        is TaskStatus.NotCompleted -> {
            when (status) {
                is TaskStatus.NotCompleted.Pending -> {
                    if (item is TaskWithExtrasAndRecordModel.Habit.HabitContinuous) {
                        when (item) {
                            is TaskWithExtrasAndRecordModel.Habit.HabitContinuous.HabitNumber -> {
                                when (val entry = item.recordEntry) {
                                    is RecordEntry.Number -> {
                                        item.taskWithExtrasModel.taskModel.progress.let { numberProgress ->
                                            (entry.number / numberProgress.limitNumber).toFloat()
                                        }
                                    }

                                    else -> NOT_COMPLETED_PROGRESS
                                }
                            }

                            is TaskWithExtrasAndRecordModel.Habit.HabitContinuous.HabitTime -> {
                                when (val entry = item.recordEntry) {
                                    is RecordEntry.Time -> {
                                        item.taskWithExtrasModel.taskModel.progress.let { timeProgress ->
                                            entry.time.toMillisecondOfDay().toFloat()
                                                .let { entryValue ->
                                                    timeProgress.limitTime.toMillisecondOfDay()
                                                        .toFloat().let { progressValue ->
                                                            entryValue / progressValue
                                                        }
                                                }
                                        }
                                    }

                                    else -> NOT_COMPLETED_PROGRESS
                                }
                            }
                        }
                    } else NOT_COMPLETED_PROGRESS
                }

                is TaskStatus.NotCompleted.Skipped -> NOT_COMPLETED_PROGRESS
                is TaskStatus.NotCompleted.Failed -> NOT_COMPLETED_PROGRESS
                else -> NOT_COMPLETED_PROGRESS
            }
        }

        else -> NOT_COMPLETED_PROGRESS
    }
}

@Composable
fun ViewScheduleScreenConfig(
    config: ViewScheduleScreenConfig,
    onEvent: (ViewScheduleScreenEvent) -> Unit,
) {
    when (config) {
        is ViewScheduleScreenConfig.PickTaskType -> {
            PickTaskTypeDialog(allTaskTypes = config.allTaskTypes) { result ->
                onEvent(ViewScheduleScreenEvent.ResultEvent.PickTaskType(result))
            }
        }

        is ViewScheduleScreenConfig.PickTaskProgressType -> {
            PickTaskProgressTypeDialog(allTaskProgressTypes = config.allProgressTypes) { result ->
                onEvent(ViewScheduleScreenEvent.ResultEvent.PickTaskProgressType(result))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenTopBar(
    onMenuClick: () -> Unit,
    onSearchClick: () -> Unit,
    onPickDateClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = "Apr 14, 2024")
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    painter = painterResource(id = com.example.inhabitroutine.core.presentation.R.drawable.ic_menu),
                    contentDescription = null
                )
            }
        },
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(
                    painter = painterResource(id = com.example.inhabitroutine.core.presentation.R.drawable.ic_search),
                    contentDescription = null
                )
            }
            IconButton(onClick = onPickDateClick) {
                Icon(
                    painter = painterResource(id = com.example.inhabitroutine.core.presentation.R.drawable.ic_pick_date),
                    contentDescription = null
                )
            }
        }
    )
}
