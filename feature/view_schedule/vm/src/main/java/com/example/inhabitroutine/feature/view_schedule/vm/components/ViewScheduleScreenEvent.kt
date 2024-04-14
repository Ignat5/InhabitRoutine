package com.example.inhabitroutine.feature.view_schedule.vm.components

import com.example.inhabitroutine.core.presentation.components.event.ScreenEvent
import com.example.inhabitroutine.core.presentation.components.result.ScreenResult
import com.example.inhabitroutine.domain.model.task.type.TaskType

sealed interface ViewScheduleScreenEvent : ScreenEvent {
    data object OnCreateTaskClick : ViewScheduleScreenEvent
    sealed interface PickTaskTypeResult : ViewScheduleScreenEvent {
        data class Confirm(val taskType: TaskType) : PickTaskTypeResult
        data object Dismiss : PickTaskTypeResult
    }
}