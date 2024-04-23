package com.example.inhabitroutine.feature.view_schedule.components

import com.example.inhabitroutine.core.presentation.components.event.ScreenEvent
import com.example.inhabitroutine.core.presentation.components.result.ScreenResult
import com.example.inhabitroutine.core.presentation.ui.dialog.pick_task_progress_type.PickTaskProgressTypeScreenResult
import com.example.inhabitroutine.core.presentation.ui.dialog.pick_task_type.PickTaskTypeScreenResult
import kotlinx.datetime.LocalDate

sealed interface ViewScheduleScreenEvent : ScreenEvent {
    data object OnNextWeekClick : ViewScheduleScreenEvent
    data object OnPrevWeekClick : ViewScheduleScreenEvent
    data class OnDateClick(val date: LocalDate) : ViewScheduleScreenEvent

    data object OnCreateTaskClick : ViewScheduleScreenEvent
    data object OnSearchClick : ViewScheduleScreenEvent

    sealed interface ResultEvent : ViewScheduleScreenEvent {
        val result: ScreenResult

        data class PickTaskType(override val result: PickTaskTypeScreenResult) : ResultEvent
        data class PickTaskProgressType(
            override val result: PickTaskProgressTypeScreenResult
        ) : ResultEvent
    }
}