package com.ignatlegostaev.inhabitroutine.feature.view_habits

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ignatlegostaev.inhabitroutine.core.presentation.R
import com.ignatlegostaev.inhabitroutine.core.presentation.model.UIResultModel
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.common.BaseSnackBar
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.common.ChipTaskEndDate
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.common.ChipTaskFrequency
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.common.ChipTaskProgressType
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.common.ChipTaskStartDate
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.common.ChipTaskType
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.common.CreateTaskFAB
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.common.BaseEmptyStateMessage
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.common.BaseFilterChipWithDropdownMenu
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.common.BaseTaskDefaults
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.common.ChipTaskPriority
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.common.TaskDivider
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.archive_task.ArchiveTaskDialog
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.delete_task.DeleteTaskDialog
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.pick_task_progress_type.PickTaskProgressTypeDialog
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.model.util.DomainConst
import com.ignatlegostaev.inhabitroutine.feature.view_habits.components.ViewHabitsScreenConfig
import com.ignatlegostaev.inhabitroutine.feature.view_habits.components.ViewHabitsScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.view_habits.components.ViewHabitsScreenState
import com.ignatlegostaev.inhabitroutine.feature.view_habits.config.view_habit_actions.ViewHabitActionsDialog
import com.ignatlegostaev.inhabitroutine.feature.view_habits.model.HabitFilterByStatus
import com.ignatlegostaev.inhabitroutine.feature.view_habits.model.HabitSort
import com.ignatlegostaev.inhabitroutine.feature.view_habits.model.ViewHabitsMessage
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ViewHabitsScreen(
    state: ViewHabitsScreenState,
    onEvent: (ViewHabitsScreenEvent) -> Unit,
    onMenuClick: () -> Unit
) {
    val snackBarHostState = remember { SnackbarHostState() }
    Scaffold(
        topBar = {
            ScreenTopBar(
                onMenuClick = onMenuClick,
                onSearchClick = {
                    onEvent(ViewHabitsScreenEvent.OnSearchClick)
                }
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                snackbar = { data ->
                    BaseSnackBar(snackBarData = data)
                }
            )
        },
        floatingActionButton = {
            CreateTaskFAB(
                onClick = {
                    onEvent(ViewHabitsScreenEvent.OnCreateHabitClick)
                }
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            val allHabits = remember(state.allHabitsResult) {
                state.allHabitsResult.data ?: emptyList()
            }
            Column(modifier = Modifier.fillMaxWidth()) {
                FilterSortRow(
                    sort = state.sort,
                    onSortClick = {
                        onEvent(ViewHabitsScreenEvent.OnPickSort(it))
                    },
                    filterByStatus = state.filterByStatus,
                    onFilterClick = {
                        onEvent(ViewHabitsScreenEvent.OnPickFilterByStatus(it))
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    itemsIndexed(
                        items = allHabits,
                        key = { _, item -> item.id }
                    ) { index, item ->
                        Column(
                            modifier = Modifier
                                .animateItemPlacement(
                                    animationSpec = BaseTaskDefaults.taskItemPlacementAnimationSpec
                                )
                                .fillMaxWidth()
                        ) {
                            ItemHabit(
                                item = item,
                                onClick = {
                                    onEvent(ViewHabitsScreenEvent.OnHabitClick(item.id))
                                }
                            )
                            if (index != allHabits.lastIndex) {
                                TaskDivider()
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(BaseTaskDefaults.TASK_LIST_FLOOR_SPACER_HEIGHT.dp))
                    }
                }
            }
            NoHabitsMessage(
                allHabitsResult = state.allHabitsResult,
                filterByStatus = state.filterByStatus
            )
            SnackBarMessageHandler(
                message = state.message,
                snackBarHostState = snackBarHostState,
                onMessageShown = {
                    onEvent(ViewHabitsScreenEvent.OnMessageShown)
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ItemHabit(
    item: TaskModel.Habit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onClick
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            HabitTitleRow(item.title)
            HabitDetailRow(item)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HabitDetailRow(
    item: TaskModel.Habit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ChipTaskType(item.type)
        ChipTaskProgressType(item.progressType)
        if (item.priority != DomainConst.DEFAULT_TASK_PRIORITY) {
            ChipTaskPriority(item.priority)
        }
        ChipTaskStartDate(item.date.startDate)
        item.date.endDate?.let { endDate ->
            ChipTaskEndDate(endDate)
        }
        ChipTaskFrequency(item.frequency)
    }
}

@Composable
private fun HabitTitleRow(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.fillMaxWidth(),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun FilterSortRow(
    sort: HabitSort,
    onSortClick: (HabitSort) -> Unit,
    filterByStatus: HabitFilterByStatus?,
    onFilterClick: (HabitFilterByStatus) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        item {
            SortChip(
                sort = sort,
                onSortClick = onSortClick
            )
        }
        item {
            FilterByStatusChip(
                filterByStatus = filterByStatus,
                onFilterClick = onFilterClick
            )
        }
    }
}

@Composable
private fun FilterByStatusChip(
    filterByStatus: HabitFilterByStatus?,
    onFilterClick: (HabitFilterByStatus) -> Unit
) {
    val context = LocalContext.current
    val allItems = remember { HabitFilterByStatus.entries }
    val isFilterActive = remember(filterByStatus) {
        filterByStatus != null
    }
    BaseFilterChipWithDropdownMenu(
        allItems = allItems,
        currentItem = filterByStatus,
        textByItem = { taskFilterByStatus ->
            context.getString(
                when (taskFilterByStatus) {
                    HabitFilterByStatus.OnlyActive -> R.string.task_filter_by_status_only_active_title
                    HabitFilterByStatus.OnlyArchived -> R.string.task_filter_by_status_only_archived_title
                }
            )
        },
        label = {
            val titleResId = remember(filterByStatus) {
                when (filterByStatus) {
                    null -> R.string.task_filter_by_status_title
                    HabitFilterByStatus.OnlyActive -> R.string.task_filter_by_status_only_active_title
                    HabitFilterByStatus.OnlyArchived -> R.string.task_filter_by_status_only_archived_title
                }
            }
            Text(text = stringResource(id = titleResId))
        },
        isFilterActive = isFilterActive,
        onItemClick = onFilterClick,
    )
}

@Composable
private fun SortChip(
    sort: HabitSort,
    onSortClick: (HabitSort) -> Unit
) {
    val context = LocalContext.current
    val allItems = remember { HabitSort.entries }
    BaseFilterChipWithDropdownMenu(
        allItems = allItems,
        currentItem = sort,
        textByItem = { taskSort ->
            context.getString(
                when (taskSort) {
                    HabitSort.ByPriority -> R.string.task_sort_by_priority_title
                    HabitSort.ByDate -> R.string.task_sort_by_date_title
                    HabitSort.ByTitle -> R.string.task_sort_by_name_title
                }
            )
        },
        label = {
            Icon(
                painter = painterResource(id = R.drawable.ic_sort),
                contentDescription = null
            )
        },
        isFilterActive = false,
        onItemClick = onSortClick,
        showArrowDropdown = false
    )
}

@Composable
private fun BoxScope.NoHabitsMessage(
    allHabitsResult: UIResultModel<List<TaskModel.Habit>>,
    filterByStatus: HabitFilterByStatus?
) {
    val shouldShowMessage = remember(allHabitsResult) {
        allHabitsResult is UIResultModel.Data && allHabitsResult.data.isEmpty()
    }
    if (shouldShowMessage) {
        val titleResId = remember(filterByStatus) {
            if (filterByStatus == null) R.string.no_habits_message_title
            else R.string.no_habits_after_filter_message_title
        }
        val subtitleResId = remember(filterByStatus) {
            if (filterByStatus == null) R.string.no_habits_message_subtitle
            else R.string.no_habits_after_filter_message_subtitle
        }
        BaseEmptyStateMessage(
            titleResId = titleResId,
            subtitleResId = subtitleResId,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 16.dp)
        )
    }
}

@Composable
private fun SnackBarMessageHandler(
    message: ViewHabitsMessage,
    snackBarHostState: SnackbarHostState,
    onMessageShown: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    LaunchedEffect(message) {
        when (message) {
            is ViewHabitsMessage.Idle -> Unit
            is ViewHabitsMessage.Message -> {
                val messageResId = when (message) {
                    is ViewHabitsMessage.Message.ArchiveSuccess ->
                        R.string.task_action_archive_success_message

                    is ViewHabitsMessage.Message.UnarchiveSuccess ->
                        R.string.task_action_unarchive_success_message

                    is ViewHabitsMessage.Message.DeleteSuccess ->
                        R.string.task_action_delete_success_message
                }
                scope.launch {
                    snackBarHostState.showSnackbar(
                        message = context.getString(messageResId),
                        duration = SnackbarDuration.Short
                    )
                }
                onMessageShown()
            }
        }
    }
}

@Composable
fun ViewHabitsConfig(
    config: ViewHabitsScreenConfig,
    onEvent: (ViewHabitsScreenEvent) -> Unit
) {
    when (config) {
        is ViewHabitsScreenConfig.ViewHabitActions -> {
            ViewHabitActionsDialog(stateHolder = config.stateHolder) {
                onEvent(ViewHabitsScreenEvent.ResultEvent.ViewHabitActions(it))
            }
        }

        is ViewHabitsScreenConfig.PickTaskProgressType -> {
            PickTaskProgressTypeDialog(allTaskProgressTypes = config.allProgressTypes) {
                onEvent(ViewHabitsScreenEvent.ResultEvent.PickTaskProgressType(it))
            }
        }

        is ViewHabitsScreenConfig.ArchiveTask -> {
            ArchiveTaskDialog(stateHolder = config.stateHolder) {
                onEvent(ViewHabitsScreenEvent.ResultEvent.ArchiveTask(it))
            }
        }

        is ViewHabitsScreenConfig.DeleteTask -> {
            DeleteTaskDialog(stateHolder = config.stateHolder) {
                onEvent(ViewHabitsScreenEvent.ResultEvent.DeleteTask(it))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenTopBar(
    onMenuClick: () -> Unit,
    onSearchClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.view_habits_screen_title))
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(painter = painterResource(id = R.drawable.ic_menu), contentDescription = null)
            }
        },
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = null
                )
            }
        }
    )
}