package com.example.inhabitroutine.feature.view_habits

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.inhabitroutine.core.presentation.R
import com.example.inhabitroutine.core.presentation.ui.common.BaseSnackBar
import com.example.inhabitroutine.core.presentation.ui.common.ChipTaskEndDate
import com.example.inhabitroutine.core.presentation.ui.common.ChipTaskFrequency
import com.example.inhabitroutine.core.presentation.ui.common.ChipTaskProgressType
import com.example.inhabitroutine.core.presentation.ui.common.ChipTaskStartDate
import com.example.inhabitroutine.core.presentation.ui.common.ChipTaskType
import com.example.inhabitroutine.core.presentation.ui.common.CreateTaskFAB
import com.example.inhabitroutine.core.presentation.ui.common.TaskDivider
import com.example.inhabitroutine.core.presentation.ui.dialog.archive_task.ArchiveTaskDialog
import com.example.inhabitroutine.core.presentation.ui.dialog.delete_task.DeleteTaskDialog
import com.example.inhabitroutine.core.presentation.ui.dialog.pick_task_progress_type.PickTaskProgressTypeDialog
import com.example.inhabitroutine.domain.model.task.TaskModel
import com.example.inhabitroutine.feature.view_habits.components.ViewHabitsScreenConfig
import com.example.inhabitroutine.feature.view_habits.components.ViewHabitsScreenEvent
import com.example.inhabitroutine.feature.view_habits.components.ViewHabitsScreenState
import com.example.inhabitroutine.feature.view_habits.config.view_habit_actions.ViewHabitActionsDialog
import com.example.inhabitroutine.feature.view_habits.model.HabitFilterByStatus
import com.example.inhabitroutine.feature.view_habits.model.HabitSort
import com.example.inhabitroutine.feature.view_habits.model.ViewHabitsMessage
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
        floatingActionButtonPosition = FabPosition.End
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
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
                        items = state.allHabits,
                        key = { _, item -> item.id }
                    ) { index, item ->
                        Column(modifier = Modifier.fillMaxWidth()) {
                            ItemHabit(
                                item = item,
                                onClick = {
                                    onEvent(ViewHabitsScreenEvent.OnHabitClick(item.id))
                                },
                                modifier = Modifier.animateItemPlacement()
                            )
                            if (index != state.allHabits.lastIndex) {
                                TaskDivider()
                            }
                        }
                    }
                }
            }
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
        ChipTaskType(taskType = item.type)
        ChipTaskProgressType(taskProgressType = item.progressType)
        ChipTaskStartDate(date = item.date.startDate)
        item.date.endDate?.let { endDate ->
            ChipTaskEndDate(date = endDate)
        }
        ChipTaskFrequency(taskFrequency = item.frequency)
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
    val isSelected = remember(filterByStatus) {
        filterByStatus != null
    }
    var isExpanded by remember {
        mutableStateOf(false)
    }
    Box {
        FilterChip(
            selected = isSelected,
            onClick = { isExpanded = !isExpanded },
            label = {
                val titleResId = when (filterByStatus) {
                    null -> R.string.task_filter_by_status_title
                    HabitFilterByStatus.OnlyActive -> R.string.task_filter_by_status_only_active_title
                    HabitFilterByStatus.OnlyArchived -> R.string.task_filter_by_status_only_archived_title
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
            }
        )
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            val allFilterItems = remember { HabitFilterByStatus.entries }
            allFilterItems.forEach { item ->
                val titleResId = remember {
                    when (item) {
                        HabitFilterByStatus.OnlyActive -> R.string.task_filter_by_status_only_active_title
                        HabitFilterByStatus.OnlyArchived -> R.string.task_filter_by_status_only_archived_title
                    }
                }
                val isCurrent = remember {
                    item == filterByStatus
                }
                DropdownMenuItem(
                    text = {
                        Text(text = stringResource(id = titleResId))
                    },
                    onClick = {
                        onFilterClick(item)
                        isExpanded = false
                    },
                    trailingIcon = {
                        if (isCurrent) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_check),
                                modifier = Modifier.size(16.dp),
                                contentDescription = null
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun SortChip(
    sort: HabitSort,
    onSortClick: (HabitSort) -> Unit
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
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = null
                )
            }
        )
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
        ) {
            val allSortItems = remember { HabitSort.entries }
            allSortItems.forEach { item ->
                val titleResId = when (item) {
                    HabitSort.ByDate -> R.string.task_sort_by_date_title
                    HabitSort.ByTitle -> R.string.task_sort_by_name_title
                }
                val isSelected = remember {
                    item == sort
                }
                DropdownMenuItem(
                    text = {
                        Text(text = stringResource(id = titleResId))
                    },
                    onClick = {
                        onSortClick(item)
                        isExpanded = false
                    },
                    trailingIcon = {
                        if (isSelected) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_check),
                                modifier = Modifier.size(16.dp),
                                contentDescription = null
                            )
                        }
                    }
                )
            }
        }
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