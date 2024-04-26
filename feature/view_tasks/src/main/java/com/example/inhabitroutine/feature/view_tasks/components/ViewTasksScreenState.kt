package com.example.inhabitroutine.feature.view_tasks.components

import androidx.compose.runtime.Immutable
import com.example.inhabitroutine.core.presentation.components.state.ScreenState
import com.example.inhabitroutine.domain.model.task.TaskModel
import com.example.inhabitroutine.feature.view_tasks.model.TaskFilterByStatus
import com.example.inhabitroutine.feature.view_tasks.model.TaskFilterByType
import com.example.inhabitroutine.feature.view_tasks.model.TaskSort
import com.example.inhabitroutine.feature.view_tasks.model.ViewTasksMessage

@Immutable
data class ViewTasksScreenState(
    val allTasks: List<TaskModel.Task>,
    val filterByStatus: TaskFilterByStatus?,
    val filterByType: TaskFilterByType?,
    val sort: TaskSort,
    val message: ViewTasksMessage
) : ScreenState
