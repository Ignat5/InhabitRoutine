package com.ignatlegostaev.inhabitroutine.feature.search_tasks.components

import androidx.compose.runtime.Immutable
import com.ignatlegostaev.inhabitroutine.core.presentation.components.state.ScreenState
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel

@Immutable
data class SearchTasksScreenState(
    val searchQuery: String,
    val allTasks: List<TaskModel>
) : ScreenState
