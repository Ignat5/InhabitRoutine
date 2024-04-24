package com.example.inhabitroutine.feature.view_schedule.config.view_task_actions.components

import androidx.compose.runtime.Immutable
import com.example.inhabitroutine.core.presentation.components.state.ScreenState
import com.example.inhabitroutine.domain.model.derived.TaskWithExtrasAndRecordModel
import com.example.inhabitroutine.feature.view_schedule.config.view_task_actions.model.ItemTaskAction
import kotlinx.datetime.LocalDate

@Immutable
data class ViewTaskActionsScreenState(
    val taskWithExtrasAndRecordModel: TaskWithExtrasAndRecordModel,
    val allTaskActionItems: List<ItemTaskAction>,
    val date: LocalDate
) : ScreenState
