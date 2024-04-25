package com.example.inhabitroutine.feature.view_task_statistics.components

import androidx.compose.runtime.Immutable
import com.example.inhabitroutine.core.presentation.components.state.ScreenState
import com.example.inhabitroutine.domain.model.task.TaskModel
import com.example.inhabitroutine.domain.task.api.use_case.calculate_statistics.TaskStatisticsModel

@Immutable
data class ViewTaskStatisticsScreenState(
    val taskModel: TaskModel?,
    val taskStatisticsModel: TaskStatisticsModel
) : ScreenState
