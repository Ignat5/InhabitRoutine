package com.example.inhabitroutine.feature.view_tasks

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
import com.example.inhabitroutine.core.presentation.R
import com.example.inhabitroutine.core.presentation.model.UIResultModel
import com.example.inhabitroutine.core.presentation.ui.common.BaseSnackBar
import com.example.inhabitroutine.core.presentation.ui.common.ChipTaskEndDate
import com.example.inhabitroutine.core.presentation.ui.common.ChipTaskFrequency
import com.example.inhabitroutine.core.presentation.ui.common.ChipTaskProgressType
import com.example.inhabitroutine.core.presentation.ui.common.ChipTaskStartDate
import com.example.inhabitroutine.core.presentation.ui.common.ChipTaskType
import com.example.inhabitroutine.core.presentation.ui.common.CreateTaskFAB
import com.example.inhabitroutine.core.presentation.ui.common.BaseEmptyStateMessage
import com.example.inhabitroutine.core.presentation.ui.common.BaseTaskDefaults
import com.example.inhabitroutine.core.presentation.ui.common.TaskDivider
import com.example.inhabitroutine.core.presentation.ui.dialog.archive_task.ArchiveTaskDialog
import com.example.inhabitroutine.core.presentation.ui.dialog.delete_task.DeleteTaskDialog
import com.example.inhabitroutine.core.presentation.ui.dialog.pick_task_type.PickTaskTypeDialog
import com.example.inhabitroutine.domain.model.task.TaskModel
import com.example.inhabitroutine.domain.model.task.content.TaskDate
import com.example.inhabitroutine.feature.view_tasks.components.ViewTasksScreenConfig
import com.example.inhabitroutine.feature.view_tasks.components.ViewTasksScreenEvent
import com.example.inhabitroutine.feature.view_tasks.components.ViewTasksScreenState
import com.example.inhabitroutine.feature.view_tasks.config.view_task_actions.ViewTaskActionsDialog
import com.example.inhabitroutine.feature.view_tasks.model.TaskFilterByStatus
import com.example.inhabitroutine.feature.view_tasks.model.TaskFilterByType
import com.example.inhabitroutine.feature.view_tasks.model.TaskSort
import com.example.inhabitroutine.feature.view_tasks.model.ViewTasksMessage
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ViewTasksScreen(
    state: ViewTasksScreenState,
    onEvent: (ViewTasksScreenEvent) -> Unit,
    onMenuClick: () -> Unit
) {
    val snackBarHostState = remember { SnackbarHostState() }
    Scaffold(
        topBar = {
            ScreenTopBar(
                onMenuClick = onMenuClick,
                onSearchClick = {
                    onEvent(ViewTasksScreenEvent.OnSearchClick)
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
                    onEvent(ViewTasksScreenEvent.OnCreateTaskClick)
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
            val allTasks = remember(state.allTasksResult) {
                state.allTasksResult.data ?: emptyList()
            }
            Column(modifier = Modifier.fillMaxWidth()) {
                FilterSortChipRow(
                    sort = state.sort,
                    onSortClick = {
                        onEvent(ViewTasksScreenEvent.OnPickSort(it))
                    },
                    filterByStatus = state.filterByStatus,
                    onFilterByStatusClick = {
                        onEvent(ViewTasksScreenEvent.OnPickFilterByStatus(it))
                    },
                    filterByType = state.filterByType,
                    onFilterByTypeClick = {
                        onEvent(ViewTasksScreenEvent.OnPickFilterByType(it))
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    itemsIndexed(
                        items = allTasks,
                        key = { _, item -> item.id }
                    ) { index, item ->
                        Column(
                            modifier = Modifier
                                .animateItemPlacement(
                                    animationSpec = BaseTaskDefaults.taskItemPlacementAnimationSpec
                                )
                                .fillMaxWidth()
                        ) {
                            ItemTask(
                                item = item,
                                onClick = {
                                    onEvent(
                                        ViewTasksScreenEvent.OnTaskClick(
                                            item.id
                                        )
                                    )
                                }
                            )
                            if (index != allTasks.lastIndex) {
                                TaskDivider()
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(BaseTaskDefaults.TASK_LIST_FLOOR_SPACER_HEIGHT.dp))
                    }
                }
            }
            NoTasksMessage(
                allTasksResult = state.allTasksResult,
                filterByStatus = state.filterByStatus,
                filterByType = state.filterByType
            )
            SnackBarMessageHandler(
                message = state.message,
                snackBarHostState = snackBarHostState,
                onMessageShown = {
                    onEvent(ViewTasksScreenEvent.OnMessageShown)
                }
            )
        }
    }
}

@Composable
private fun BoxScope.NoTasksMessage(
    allTasksResult: UIResultModel<List<TaskModel.Task>>,
    filterByStatus: TaskFilterByStatus?,
    filterByType: TaskFilterByType?
) {
    val shouldShowMessage = remember(allTasksResult) {
        allTasksResult is UIResultModel.Data && allTasksResult.data.isEmpty()
    }
    if (shouldShowMessage) {
        val areFilterApplied = remember(filterByStatus, filterByType) {
            filterByStatus != null || filterByType != null
        }
        val titleResId = if (areFilterApplied) R.string.no_tasks_after_filter_message_title
        else R.string.no_tasks_message_title

        val subtitleResId = if (areFilterApplied) R.string.no_tasks_after_filter_message_subtitle
        else R.string.no_tasks_message_subtitle

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
    message: ViewTasksMessage,
    snackBarHostState: SnackbarHostState,
    onMessageShown: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    LaunchedEffect(message) {
        when (message) {
            is ViewTasksMessage.Idle -> Unit
            is ViewTasksMessage.Message -> {
                val messageResId = when (message) {
                    is ViewTasksMessage.Message.ArchiveSuccess ->
                        R.string.task_action_archive_success_message

                    is ViewTasksMessage.Message.UnarchiveSuccess ->
                        R.string.task_action_unarchive_success_message

                    is ViewTasksMessage.Message.DeleteSuccess ->
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ItemTask(
    item: TaskModel.Task,
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
            TaskTitleRow(item.title)
            TaskDetailsRow(item)
        }
    }
}

@Composable
private fun TaskTitleRow(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.fillMaxWidth(),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TaskDetailsRow(
    item: TaskModel.Task
) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ChipTaskType(taskType = item.type)
        ChipTaskProgressType(taskProgressType = item.progressType)
        when (val taskDate = item.date) {
            is TaskDate.Period -> {
                ChipTaskStartDate(date = taskDate.startDate)
                taskDate.endDate?.let { endDate ->
                    ChipTaskEndDate(date = endDate)
                }
            }

            is TaskDate.Day -> {
                ChipTaskStartDate(date = taskDate.date)
            }
        }
        if (item is TaskModel.RecurringActivity) {
            ChipTaskFrequency(item.frequency)
        }
    }
}

@Composable
private fun FilterSortChipRow(
    sort: TaskSort,
    onSortClick: (TaskSort) -> Unit,
    filterByStatus: TaskFilterByStatus?,
    onFilterByStatusClick: (TaskFilterByStatus) -> Unit,
    filterByType: TaskFilterByType?,
    onFilterByTypeClick: (TaskFilterByType) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
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
                onFilterClick = onFilterByStatusClick
            )
        }

        item {
            FilterByTypeChip(
                filterByType = filterByType,
                onFilterClick = onFilterByTypeClick
            )
        }
    }
}

@Composable
private fun SortChip(
    sort: TaskSort,
    onSortClick: (TaskSort) -> Unit
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }
    Box {
        FilterChip(
            selected = false,
            onClick = { isExpanded = !isExpanded },
            label = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_sort),
                    contentDescription = null
                )
            }
        )
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            val allSortItems = remember { TaskSort.entries }
            allSortItems.forEach { item ->
                val titleResId = remember {
                    when (item) {
                        TaskSort.ByDate -> R.string.task_sort_by_date_title
                        TaskSort.ByTitle -> R.string.task_sort_by_name_title
                    }
                }
                val isSelected = remember {
                    item == sort
                }
                DropdownMenuItem(
                    text = {
                        Text(text = stringResource(id = titleResId))
                    },
                    trailingIcon = {
                        if (isSelected) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_check),
                                modifier = Modifier.size(16.dp),
                                contentDescription = null
                            )
                        }
                    },
                    onClick = {
                        onSortClick(item)
                        isExpanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun FilterByStatusChip(
    filterByStatus: TaskFilterByStatus?,
    onFilterClick: (TaskFilterByStatus) -> Unit
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }
    val isSelected = remember(filterByStatus) {
        filterByStatus != null
    }
    Box {
        FilterChip(
            selected = isSelected,
            onClick = { isExpanded = !isExpanded },
            label = {
                val titleResId = remember(filterByStatus) {
                    when (filterByStatus) {
                        null -> R.string.task_filter_by_status_title
                        TaskFilterByStatus.OnlyActive -> R.string.task_filter_by_status_only_active_title
                        TaskFilterByStatus.OnlyArchived -> R.string.task_filter_by_status_only_archived_title
                    }
                }
                Text(text = stringResource(id = titleResId))
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_dropdown),
                    modifier = Modifier.rotate(
                        if (isExpanded) 180f else 0f
                    ),
                    contentDescription = null
                )
            },
        )
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            val allFilterItems = remember { TaskFilterByStatus.entries }
            allFilterItems.forEach { item ->
                val titleResId = remember {
                    when (item) {
                        TaskFilterByStatus.OnlyActive -> R.string.task_filter_by_status_only_active_title
                        TaskFilterByStatus.OnlyArchived -> R.string.task_filter_by_status_only_archived_title
                    }
                }
                val isCurrent = remember {
                    item == filterByStatus
                }
                DropdownMenuItem(
                    text = {
                        Text(text = stringResource(id = titleResId))
                    },
                    trailingIcon = {
                        if (isCurrent) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_check),
                                modifier = Modifier.size(16.dp),
                                contentDescription = null
                            )
                        }
                    },
                    onClick = {
                        onFilterClick(item)
                        isExpanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun FilterByTypeChip(
    filterByType: TaskFilterByType?,
    onFilterClick: (TaskFilterByType) -> Unit
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }
    val isSelected = remember(filterByType) {
        filterByType != null
    }
    Box {
        FilterChip(
            selected = isSelected,
            onClick = { isExpanded = !isExpanded },
            label = {
                val titleResId = remember(filterByType) {
                    when (filterByType) {
                        null -> R.string.task_filter_by_type_title
                        TaskFilterByType.OnlyRecurring -> R.string.task_filter_by_type_recurring_tasks_title
                        TaskFilterByType.OnlySingle -> R.string.task_filter_by_type_single_tasks_title
                    }
                }
                Text(text = stringResource(id = titleResId))
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_dropdown),
                    modifier = Modifier.rotate(
                        if (isExpanded) 180f else 0f
                    ),
                    contentDescription = null
                )
            },
        )
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            val allFilterItems = remember { TaskFilterByType.entries }
            allFilterItems.forEach { item ->
                val titleResId = remember {
                    when (item) {
                        TaskFilterByType.OnlyRecurring -> R.string.task_filter_by_type_recurring_tasks_title
                        TaskFilterByType.OnlySingle -> R.string.task_filter_by_type_single_tasks_title
                    }
                }
                val isCurrent = remember {
                    item == filterByType
                }
                DropdownMenuItem(
                    text = {
                        Text(text = stringResource(id = titleResId))
                    },
                    trailingIcon = {
                        if (isCurrent) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_check),
                                modifier = Modifier.size(16.dp),
                                contentDescription = null
                            )
                        }
                    },
                    onClick = {
                        onFilterClick(item)
                        isExpanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun ViewTasksConfig(
    config: ViewTasksScreenConfig,
    onEvent: (ViewTasksScreenEvent) -> Unit
) {
    when (config) {
        is ViewTasksScreenConfig.ViewTaskActions -> {
            ViewTaskActionsDialog(stateHolder = config.stateHolder) {
                onEvent(ViewTasksScreenEvent.ResultEvent.ViewTaskActions(it))
            }
        }

        is ViewTasksScreenConfig.ArchiveTask -> {
            ArchiveTaskDialog(stateHolder = config.stateHolder) {
                onEvent(ViewTasksScreenEvent.ResultEvent.ArchiveTask(it))
            }
        }

        is ViewTasksScreenConfig.DeleteTask -> {
            DeleteTaskDialog(stateHolder = config.stateHolder) {
                onEvent(ViewTasksScreenEvent.ResultEvent.DeleteTask(it))
            }
        }

        is ViewTasksScreenConfig.PickTaskType -> {
            PickTaskTypeDialog(allTaskTypes = config.allTaskTypes) {
                onEvent(ViewTasksScreenEvent.ResultEvent.PickTaskType(it))
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
            Text(text = stringResource(id = R.string.view_tasks_screen_title))
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_menu),
                    contentDescription = null
                )
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