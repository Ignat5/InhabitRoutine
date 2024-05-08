package com.ignatlegostaev.inhabitroutine.feature.view_tasks.config.view_task_actions.components

import androidx.compose.runtime.Immutable
import com.ignatlegostaev.inhabitroutine.core.presentation.components.state.ScreenState
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.feature.view_tasks.config.view_task_actions.model.ItemTaskAction

@Immutable
data class ViewTaskActionsScreenState(
    val taskModel: TaskModel.Task,
    val allTaskActionItems: List<ItemTaskAction>
) : ScreenState
