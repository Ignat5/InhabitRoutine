package com.example.inhabitroutine.feature.view_schedule.components

import com.example.inhabitroutine.core.presentation.components.event.ScreenEvent
import com.example.inhabitroutine.core.presentation.components.result.ScreenResult
import com.example.inhabitroutine.core.presentation.ui.dialog.pick_task_type.PickTaskTypeScreenResult

sealed interface ViewScheduleScreenEvent : ScreenEvent {
    data object OnCreateTaskClick : ViewScheduleScreenEvent

    sealed interface ResultEvent : ViewScheduleScreenEvent {
        val result: ScreenResult

        data class PickTaskType(override val result: PickTaskTypeScreenResult) : ResultEvent
    }
}