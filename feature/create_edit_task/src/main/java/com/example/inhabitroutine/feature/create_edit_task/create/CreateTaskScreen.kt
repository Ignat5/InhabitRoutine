package com.example.inhabitroutine.feature.create_edit_task.create

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.inhabitroutine.core.presentation.R
import com.example.inhabitroutine.domain.model.task.type.TaskType
import com.example.inhabitroutine.feature.create_edit_task.base.BaseCreateEditTaskConfig
import com.example.inhabitroutine.feature.create_edit_task.base.baseConfigItems
import com.example.inhabitroutine.feature.create_edit_task.base.components.BaseCreateEditTaskScreenEvent
import com.example.inhabitroutine.feature.create_edit_task.create.components.CreateTaskScreenConfig
import com.example.inhabitroutine.feature.create_edit_task.create.components.CreateTaskScreenEvent
import com.example.inhabitroutine.feature.create_edit_task.create.components.CreateTaskScreenState
import com.example.inhabitroutine.feature.create_edit_task.create.config.ConfirmLeavingDialog

@Composable
fun CreateTaskScreen(
    state: CreateTaskScreenState,
    onEvent: (CreateTaskScreenEvent) -> Unit
) {
    BackHandler {
        onEvent(CreateTaskScreenEvent.OnLeaveRequest)
    }
    Scaffold(
        topBar = {
            ScreenTopBar(
                taskType = state.taskModel?.type,
                canSave = state.canSave,
                onSaveClick = {
                    onEvent(CreateTaskScreenEvent.OnSaveClick)
                },
                onCloseClick = {
                    onEvent(CreateTaskScreenEvent.OnLeaveRequest)
                }
            )
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                baseConfigItems(
                    allTaskConfigItems = state.allTaskConfigItems,
                    onItemClick = { item ->
                        onEvent(
                            CreateTaskScreenEvent.Base(
                                BaseCreateEditTaskScreenEvent.OnItemConfigClick(item)
                            )
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun CreateTaskConfig(
    config: CreateTaskScreenConfig,
    onEvent: (CreateTaskScreenEvent) -> Unit
) {
    when (config) {
        is CreateTaskScreenConfig.Base -> {
            BaseCreateEditTaskConfig(
                config = config.baseConfig,
                onEvent = {
                    onEvent(CreateTaskScreenEvent.Base(it))
                }
            )
        }

        is CreateTaskScreenConfig.ConfirmLeaving -> {
            ConfirmLeavingDialog(
                onResult = {
                    onEvent(CreateTaskScreenEvent.ResultEvent.ConfirmLeaving(it))
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenTopBar(
    taskType: TaskType?,
    canSave: Boolean,
    onSaveClick: () -> Unit,
    onCloseClick: () -> Unit
) {
    TopAppBar(
        title = {
            val titleId = remember(taskType) {
                when (taskType) {
                    null -> null
                    TaskType.Habit -> R.string.create_habit_title
                    TaskType.SingleTask, TaskType.RecurringTask -> R.string.create_task_title
                }
            }
            Text(
                text = titleId?.let {
                    stringResource(id = it)
                } ?: ""
            )
        },
        navigationIcon = {
            IconButton(onClick = onCloseClick) {
                Icon(
                    painter = painterResource(id = com.example.inhabitroutine.core.presentation.R.drawable.ic_close),
                    contentDescription = null
                )
            }
        },
        actions = {
            TextButton(
                onClick = onSaveClick,
                enabled = canSave
            ) {
                Text(text = stringResource(id = com.example.inhabitroutine.core.presentation.R.string.save))
            }
        }
    )
}