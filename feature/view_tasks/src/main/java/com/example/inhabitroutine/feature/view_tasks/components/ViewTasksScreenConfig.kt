package com.example.inhabitroutine.feature.view_tasks.components

import com.example.inhabitroutine.core.presentation.components.config.ScreenConfig
import com.example.inhabitroutine.domain.model.task.type.TaskType

sealed interface ViewTasksScreenConfig : ScreenConfig {
    data class PickTaskType(
        val allTaskTypes: List<TaskType>
    ) : ViewTasksScreenConfig
}