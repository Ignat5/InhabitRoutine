package com.ignatlegostaev.inhabitroutine.feature.create_edit_task.edit

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.ignatlegostaev.inhabitroutine.core.presentation.R
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.common.BaseSnackBar
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.archive_task.ArchiveTaskDialog
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.delete_task.DeleteTaskDialog
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.reset_task.ResetTaskDialog
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.BaseCreateEditTaskConfig
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.baseConfigItems
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.components.BaseCreateEditTaskScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.edit.components.EditTaskScreenConfig
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.edit.components.EditTaskScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.edit.components.EditTaskScreenState
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.edit.model.EditTaskMessage
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.edit.model.ItemTaskAction
import kotlinx.coroutines.launch

@Composable
fun EditTaskScreen(
    state: EditTaskScreenState,
    onEvent: (EditTaskScreenEvent) -> Unit
) {
    val snackBarHostState = remember { SnackbarHostState() }
    BackHandler { onEvent(EditTaskScreenEvent.OnBackRequest) }
    Scaffold(
        topBar = {
            ScreenTopBar(
                taskTitle = state.taskModel?.title,
                allTaskActionItems = state.allTaskActionItems,
                onItemActionClick = {
                    onEvent(EditTaskScreenEvent.OnItemActionClick(it))
                },
                onBack = {
                    onEvent(EditTaskScreenEvent.OnBackRequest)
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
        modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                baseConfigItems(
                    allTaskConfigItems = state.allTaskConfigItems,
                    onItemClick = { item ->
                        onEvent(
                            EditTaskScreenEvent.Base(
                                BaseCreateEditTaskScreenEvent.OnItemConfigClick(item)
                            )
                        )
                    }
                )
            }
        }
        SnackBarMessageHandler(
            message = state.message,
            snackBarHostState = snackBarHostState,
            onMessageShown = {
                onEvent(EditTaskScreenEvent.OnMessageShown)
            }
        )
    }
}

@Composable
private fun SnackBarMessageHandler(
    message: EditTaskMessage,
    snackBarHostState: SnackbarHostState,
    onMessageShown: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    LaunchedEffect(message) {
        when (message) {
            is EditTaskMessage.Idle -> Unit
            is EditTaskMessage.Message -> {
                val messageTextResId = when (message) {
                    is EditTaskMessage.Message.ArchiveSuccess ->
                        R.string.task_action_archive_success_message

                    is EditTaskMessage.Message.UnarchiveSuccess ->
                        R.string.task_action_unarchive_success_message

                    is EditTaskMessage.Message.ResetSuccess ->
                        R.string.task_action_reset_success_message
                }
                scope.launch {
                    snackBarHostState.showSnackbar(
                        message = context.getString(messageTextResId),
                        duration = SnackbarDuration.Short
                    )
                }
                onMessageShown()
            }
        }
    }
}

@Composable
fun EditTaskConfig(
    config: EditTaskScreenConfig,
    onEvent: (EditTaskScreenEvent) -> Unit
) {
    when (config) {
        is EditTaskScreenConfig.Base -> {
            BaseCreateEditTaskConfig(config = config.baseConfig) {
                onEvent(EditTaskScreenEvent.Base(it))
            }
        }

        is EditTaskScreenConfig.ArchiveTask -> {
            ArchiveTaskDialog(stateHolder = config.stateHolder) {
                onEvent(EditTaskScreenEvent.ResultEvent.ArchiveTask(it))
            }
        }

        is EditTaskScreenConfig.DeleteTask -> {
            DeleteTaskDialog(stateHolder = config.stateHolder) {
                onEvent(EditTaskScreenEvent.ResultEvent.DeleteTask(it))
            }
        }

        is EditTaskScreenConfig.ResetTask -> {
            ResetTaskDialog(stateHolder = config.stateHolder) {
                onEvent(EditTaskScreenEvent.ResultEvent.ResetTask(it))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenTopBar(
    taskTitle: String?,
    allTaskActionItems: List<ItemTaskAction>,
    onItemActionClick: (ItemTaskAction) -> Unit,
    onBack: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = taskTitle ?: "",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null
                )
            }
        },
        actions = {
            var isExpanded by remember {
                mutableStateOf(false)
            }
            Box {
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_more),
                        contentDescription = null
                    )
                }
                DropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false }
                ) {
                    allTaskActionItems.forEach { item ->
                        DropdownMenuItem(
                            text = {
                                Text(text = stringResource(id = item.toTitleResId()))
                            },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = item.toIconResId()),
                                    contentDescription = null
                                )
                            },
                            onClick = {
                                onItemActionClick(item)
                                isExpanded = false
                            }
                        )
                    }
                }
            }
        }
    )
}

private fun ItemTaskAction.toTitleResId() = when (this) {
    is ItemTaskAction.ViewStatistics -> R.string.task_action_view_statistics
    is ItemTaskAction.ArchiveUnarchive.Archive -> R.string.task_action_archive
    is ItemTaskAction.ArchiveUnarchive.Unarchive -> R.string.task_action_unarchive
    is ItemTaskAction.Reset -> R.string.task_action_restart
    is ItemTaskAction.Delete -> R.string.task_action_delete
}

private fun ItemTaskAction.toIconResId() = when (this) {
    is ItemTaskAction.ViewStatistics -> R.drawable.ic_statistics
    is ItemTaskAction.ArchiveUnarchive.Archive -> R.drawable.ic_archive
    is ItemTaskAction.ArchiveUnarchive.Unarchive -> R.drawable.ic_unarchive
    is ItemTaskAction.Reset -> R.drawable.ic_reset
    is ItemTaskAction.Delete -> R.drawable.ic_delete
}