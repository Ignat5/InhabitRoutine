package com.ignatlegostaev.inhabitroutine.feature.view_schedule.config.view_task_actions.components

import androidx.compose.runtime.Immutable
import com.ignatlegostaev.inhabitroutine.core.presentation.components.state.ScreenState
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.feature.view_schedule.config.view_task_actions.model.ItemTaskAction
import kotlinx.datetime.LocalDate

@Immutable
data class ViewTaskActionsScreenState(
    val task: TaskModel,
    val allTaskActionItems: List<ItemTaskAction>,
    val date: LocalDate
) : ScreenState
