package com.ignatlegostaev.inhabitroutine.feature.view_tasks.components

import androidx.compose.runtime.Immutable
import com.ignatlegostaev.inhabitroutine.core.presentation.components.state.ScreenState
import com.ignatlegostaev.inhabitroutine.core.presentation.model.UIResultModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.feature.view_tasks.model.TaskFilterByStatus
import com.ignatlegostaev.inhabitroutine.feature.view_tasks.model.TaskFilterByType
import com.ignatlegostaev.inhabitroutine.feature.view_tasks.model.TaskSort
import com.ignatlegostaev.inhabitroutine.feature.view_tasks.model.ViewTasksMessage

@Immutable
data class ViewTasksScreenState(
    val allTasksResult: UIResultModel<List<TaskModel.Task>>,
    val filterByStatus: TaskFilterByStatus?,
    val filterByType: TaskFilterByType?,
    val sort: TaskSort,
    val message: ViewTasksMessage
) : ScreenState
