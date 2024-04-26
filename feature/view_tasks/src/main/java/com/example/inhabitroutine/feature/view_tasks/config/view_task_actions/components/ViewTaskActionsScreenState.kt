package com.example.inhabitroutine.feature.view_tasks.config.view_task_actions.components

import androidx.compose.runtime.Immutable
import com.example.inhabitroutine.core.presentation.components.state.ScreenState
import com.example.inhabitroutine.domain.model.task.TaskModel
import com.example.inhabitroutine.feature.view_tasks.config.view_task_actions.model.ItemTaskAction

@Immutable
data class ViewTaskActionsScreenState(
    val taskModel: TaskModel.Task,
    val allTaskActionItems: List<ItemTaskAction>
) : ScreenState
