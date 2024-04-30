package com.example.inhabitroutine.feature.view_schedule

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.inhabitroutine.core.presentation.R
import com.example.inhabitroutine.core.presentation.model.UIResultModel
import com.example.inhabitroutine.core.presentation.ui.common.ChipTaskProgressType
import com.example.inhabitroutine.core.presentation.ui.common.ChipTaskReminders
import com.example.inhabitroutine.core.presentation.ui.common.ChipTaskType
import com.example.inhabitroutine.core.presentation.ui.common.CreateTaskFAB
import com.example.inhabitroutine.core.presentation.ui.common.EmptyStateMessage
import com.example.inhabitroutine.core.presentation.ui.common.TaskDivider
import com.example.inhabitroutine.core.presentation.ui.dialog.pick_task_progress_type.PickTaskProgressTypeDialog
import com.example.inhabitroutine.core.presentation.ui.dialog.pick_task_type.PickTaskTypeDialog
import com.example.inhabitroutine.core.presentation.ui.util.limitNumberToDisplay
import com.example.inhabitroutine.core.presentation.ui.util.toDisplay
import com.example.inhabitroutine.core.presentation.ui.util.toHourMinute
import com.example.inhabitroutine.core.presentation.ui.util.toMonthDayYearDisplay
import com.example.inhabitroutine.domain.model.derived.TaskStatus
import com.example.inhabitroutine.domain.model.derived.TaskWithExtrasAndRecordModel
import com.example.inhabitroutine.domain.model.record.content.RecordEntry
import com.example.inhabitroutine.domain.model.task.type.ProgressLimitType
import com.example.inhabitroutine.feature.view_schedule.components.ViewScheduleScreenConfig
import com.example.inhabitroutine.feature.view_schedule.components.ViewScheduleScreenEvent
import com.example.inhabitroutine.feature.view_schedule.components.ViewScheduleScreenState
import com.example.inhabitroutine.feature.view_schedule.config.enter_number_record.EnterTaskNumberRecordDialog
import com.example.inhabitroutine.feature.view_schedule.config.enter_time_record.EnterTaskTimeRecordDialog
import com.example.inhabitroutine.feature.view_schedule.config.view_task_actions.ViewTaskActionsDialog
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

private const val PROGRESS_INDICATOR_SIZE_DP = 32
private const val PROGRESS_INDICATOR_STROKE_WIDTH_DP = 2

private const val COMPLETED_PROGRESS = 1f
private const val NOT_COMPLETED_PROGRESS = 0f

private const val WEEK_DAY_COUNT = 7
private const val WEEK_ITEM_HEIGHT_DP = 48
private const val WEEK_ITEM_TITLE_LENGTH = 3

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ViewScheduleScreen(
    state: ViewScheduleScreenState,
    onEvent: (ViewScheduleScreenEvent) -> Unit,
    onMenuClick: () -> Unit
) {
    Scaffold(
        topBar = {
            ScreenTopBar(
                currentDate = state.currentDate,
                onMenuClick = onMenuClick,
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
            val allTasks =
                remember(state.allTasksResult) { state.allTasksResult.data ?: emptyList() }
            Column(modifier = Modifier.fillMaxWidth()) {
                WeekRow(
                    startOfWeekDate = state.startOfWeekDate,
                    currentDate = state.currentDate,
                    todayDate = state.todayDate,
                    onDateClick = {
                        onEvent(ViewScheduleScreenEvent.OnDateClick(it))
                    },
                    onNextClick = {
                        onEvent(ViewScheduleScreenEvent.OnNextWeekClick)
                    },
                    onPrevClick = {
                        onEvent(ViewScheduleScreenEvent.OnPrevWeekClick)
                    },
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    itemsIndexed(
                        items = allTasks,
                        key = { _, item -> item.task.id }
                    ) { index, item ->
                        Column(modifier = Modifier.fillMaxWidth()) {
                            ItemTask(
                                item = item,
                                context = context,
                                onClick = {
                                    onEvent(ViewScheduleScreenEvent.OnTaskClick(item.task.id))
                                },
                                onLongClick = {
                                    onEvent(ViewScheduleScreenEvent.OnTaskLongClick(item.task.id))
                                },
                                modifier = Modifier.animateItemPlacement()
                            )
                            if (index != allTasks.lastIndex) {
                                TaskDivider()
                            }
                        }
                    }
                }
            }
            NoTasksMessage(state.allTasksResult)
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
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = item.task.title,
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
        ChipTaskType(item.task.type)
        ChipTaskProgressType(item.task.progressType)
        val allReminders = remember(item.taskExtras.allReminders) {
            item.taskExtras.allReminders
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
                                    item.task.progress.let { progress ->
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
                        is TaskStatus.Completed -> null //context.getString(R.string.task_status_done)
                        is TaskStatus.NotCompleted.Pending -> null
                        is TaskStatus.NotCompleted.Skipped -> context.getString(R.string.task_status_skipped)
                        is TaskStatus.NotCompleted.Failed -> context.getString(R.string.task_status_failed)
                    }
                }
            }
        }

        is TaskWithExtrasAndRecordModel.Task -> {
            null
//            when (item.status) {
//                is TaskStatus.Completed -> context.getString(R.string.task_status_done)
//                is TaskStatus.NotCompleted.Pending -> null
//            }
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
    val containerColor = when (item.status) {
        is TaskStatus.NotCompleted.Failed -> MaterialTheme.colorScheme.errorContainer
        is TaskStatus.NotCompleted.Skipped -> MaterialTheme.colorScheme.surfaceVariant
        else -> MaterialTheme.colorScheme.primaryContainer
    }
    val progressState by animateFloatAsState(
        targetValue = progress,
        animationSpec = spring(stiffness = Spring.StiffnessVeryLow),
        label = ""
    )
    val containerColorState by animateColorAsState(
        targetValue = containerColor,
        animationSpec = spring(stiffness = Spring.StiffnessVeryLow)
    )
    CircularProgressIndicator(
        progress = { progressState },
        modifier = Modifier.size(PROGRESS_INDICATOR_SIZE_DP.dp),
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        trackColor = containerColorState,
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
                                        item.task.progress.let { numberProgress ->
                                            when (numberProgress.limitType) {
                                                ProgressLimitType.AtLeast -> {
                                                    (entry.number / numberProgress.limitNumber).toFloat()
                                                }

                                                ProgressLimitType.NoMoreThan -> NOT_COMPLETED_PROGRESS
                                                ProgressLimitType.Exactly -> NOT_COMPLETED_PROGRESS
                                            }
                                        }
                                    }

                                    else -> NOT_COMPLETED_PROGRESS
                                }
                            }

                            is TaskWithExtrasAndRecordModel.Habit.HabitContinuous.HabitTime -> {
                                when (val entry = item.recordEntry) {
                                    is RecordEntry.Time -> {
                                        item.task.progress.let { timeProgress ->
                                            when (timeProgress.limitType) {
                                                ProgressLimitType.AtLeast -> {
                                                    entry.time.toMillisecondOfDay().toFloat()
                                                        .let { entryValue ->
                                                            timeProgress.limitTime.toMillisecondOfDay()
                                                                .toFloat().let { progressValue ->
                                                                    entryValue / progressValue
                                                                }
                                                        }
                                                }

                                                ProgressLimitType.NoMoreThan -> NOT_COMPLETED_PROGRESS
                                                ProgressLimitType.Exactly -> NOT_COMPLETED_PROGRESS
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
private fun WeekRow(
    startOfWeekDate: LocalDate,
    currentDate: LocalDate,
    todayDate: LocalDate,
    onDateClick: (LocalDate) -> Unit,
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NextPrevButton(
            iconId = R.drawable.ic_previous,
            onClick = onPrevClick
        )
        AnimatedContent(
            targetState = startOfWeekDate,
            transitionSpec = {
                val towards =
                    if (targetState > initialState) AnimatedContentTransitionScope.SlideDirection.Start
                    else AnimatedContentTransitionScope.SlideDirection.End
                this.slideIntoContainer(
                    towards = towards,
                    animationSpec = tween(durationMillis = 200, delayMillis = 90)
                ) + fadeIn(
                    animationSpec = tween(durationMillis = 200, delayMillis = 90)
                ) + scaleIn(
                    animationSpec = tween(durationMillis = 200, delayMillis = 90),
                    initialScale = 0.92f
                ) togetherWith slideOutOfContainer(
                    towards = towards,
                    animationSpec = tween(durationMillis = 200, delayMillis = 90)
                ) + fadeOut(
                    animationSpec = tween(durationMillis = 200, delayMillis = 90)
                ) + scaleOut(
                    animationSpec = tween(durationMillis = 200, delayMillis = 90),
                )
            },
            contentKey = { it.toEpochDays() },
            modifier = Modifier.weight(1f)
        ) {
            DaysOfWeekRow(
                startOfWeekDate = it,
                currentDate = currentDate,
                todayDate = todayDate,
                context = context,
                onDateClick = onDateClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
        NextPrevButton(
            iconId = R.drawable.ic_next,
            onClick = onNextClick
        )
    }
}

@Composable
private fun DaysOfWeekRow(
    startOfWeekDate: LocalDate,
    currentDate: LocalDate,
    todayDate: LocalDate,
    context: Context,
    onDateClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(WEEK_DAY_COUNT),
        modifier = modifier.height(WEEK_ITEM_HEIGHT_DP.dp)
    ) {
        items(WEEK_DAY_COUNT) { offset ->
            val itemDate = remember(startOfWeekDate) {
                startOfWeekDate.plus(offset, DateTimeUnit.DAY)
            }
            val isCurrent = remember(startOfWeekDate, currentDate) {
                itemDate == currentDate
            }
            val isToday = remember(startOfWeekDate, todayDate) {
                itemDate == todayDate
            }
            ItemDayOfWeek(
                date = itemDate,
                isCurrent = isCurrent,
                isToday = isToday,
                context = context,
                onClick = {
                    onDateClick(itemDate)
                }
            )
        }
    }
}

@Composable
private fun ItemDayOfWeek(
    date: LocalDate,
    isCurrent: Boolean,
    isToday: Boolean,
    context: Context,
    onClick: () -> Unit
) {
    val dayOfWeekText = remember(date) {
        date.dayOfWeek.toDisplay(context).take(WEEK_ITEM_TITLE_LENGTH)
    }
    val dayOfMonthText = remember(date) {
        "${date.dayOfMonth}"
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(MaterialTheme.shapes.small)
            .padding(horizontal = 4.dp)
            .clickable { onClick() }
            .then(
                when {
                    isCurrent -> Modifier
                        .background(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.shapes.small
                        )

                    isToday -> Modifier
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.shapes.small
                        )

                    else -> Modifier
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(4.dp)
        ) {
            Text(
                text = dayOfWeekText,
                style = MaterialTheme.typography.labelSmall,
                color = when {
                    isCurrent -> MaterialTheme.colorScheme.onPrimary
                    isToday -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.onSurface
                }
            )
            Text(
                text = dayOfMonthText,
                style = MaterialTheme.typography.titleSmall,
                color = when {
                    isCurrent -> MaterialTheme.colorScheme.onPrimary
                    isToday -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.onSurface
                }
            )
        }
    }
}

@Composable
private fun NextPrevButton(
    @DrawableRes iconId: Int,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(id = iconId),
            tint = MaterialTheme.colorScheme.onSurface,
            contentDescription = null
        )
    }
}

@Composable
private fun BoxScope.NoTasksMessage(allTasksResult: UIResultModel<List<TaskWithExtrasAndRecordModel>>) {
    val shouldShowNoTasksMessage = remember(allTasksResult) {
        allTasksResult is UIResultModel.Data && allTasksResult.data.isEmpty()
    }
    if (shouldShowNoTasksMessage) {
        EmptyStateMessage(
            titleResId = R.string.no_tasks_scheduled_message_title,
            subtitleResId = R.string.no_tasks_scheduled_message_subtitle,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun ViewScheduleScreenConfig(
    config: ViewScheduleScreenConfig,
    onEvent: (ViewScheduleScreenEvent) -> Unit
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

        is ViewScheduleScreenConfig.EnterTaskNumberRecord -> {
            EnterTaskNumberRecordDialog(stateHolder = config.stateHolder) {
                onEvent(ViewScheduleScreenEvent.ResultEvent.EnterTaskNumberRecord(it))
            }
        }

        is ViewScheduleScreenConfig.EnterTaskTimeRecord -> {
            EnterTaskTimeRecordDialog(stateHolder = config.stateHolder) {
                onEvent(ViewScheduleScreenEvent.ResultEvent.EnterTaskTimeRecord(it))
            }
        }

        is ViewScheduleScreenConfig.ViewTaskActions -> {
            ViewTaskActionsDialog(stateHolder = config.stateHolder) {
                onEvent(ViewScheduleScreenEvent.ResultEvent.ViewTaskActions(it))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenTopBar(
    currentDate: LocalDate,
    onMenuClick: () -> Unit,
    onSearchClick: () -> Unit,
    onPickDateClick: () -> Unit
) {
    val context: Context = LocalContext.current
    val currentDateText = remember(currentDate) {
        currentDate.toMonthDayYearDisplay(context)
    }
    TopAppBar(
        title = {
            Text(text = currentDateText)
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
//            IconButton(onClick = onPickDateClick) {
//                Icon(
//                    painter = painterResource(id = com.example.inhabitroutine.core.presentation.R.drawable.ic_pick_date),
//                    contentDescription = null
//                )
//            }
        }
    )
}
