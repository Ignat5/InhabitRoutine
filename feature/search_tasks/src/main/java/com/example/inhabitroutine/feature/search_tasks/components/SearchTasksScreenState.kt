package com.example.inhabitroutine.feature.search_tasks.components

import androidx.compose.runtime.Immutable
import com.example.inhabitroutine.core.presentation.components.state.ScreenState
import com.example.inhabitroutine.domain.model.task.TaskModel

@Immutable
data class SearchTasksScreenState(
    val searchQuery: String,
    val allTasks: List<TaskModel>
) : ScreenState
