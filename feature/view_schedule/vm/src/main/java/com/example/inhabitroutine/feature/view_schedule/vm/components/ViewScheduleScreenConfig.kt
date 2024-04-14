package com.example.inhabitroutine.feature.view_schedule.vm.components

import com.example.inhabitroutine.core.presentation.components.config.ScreenConfig
import com.example.inhabitroutine.domain.model.task.type.TaskType

sealed interface ViewScheduleScreenConfig : ScreenConfig {
    data class PickTaskType(val allTaskTypes: List<TaskType>) : ViewScheduleScreenConfig
}