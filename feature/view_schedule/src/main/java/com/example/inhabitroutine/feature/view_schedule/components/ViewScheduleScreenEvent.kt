package com.example.inhabitroutine.feature.view_schedule.components

import com.example.inhabitroutine.core.presentation.components.event.ScreenEvent
import com.example.inhabitroutine.core.presentation.components.result.ScreenResult
import com.example.inhabitroutine.core.presentation.ui.dialog.pick_task_progress_type.PickTaskProgressTypeScreenResult
import com.example.inhabitroutine.core.presentation.ui.dialog.pick_task_type.PickTaskTypeScreenResult
import com.example.inhabitroutine.feature.view_schedule.config.enter_number_record.components.EnterTaskNumberRecordScreenResult
import com.example.inhabitroutine.feature.view_schedule.config.enter_time_record.components.EnterTaskTimeRecordScreenResult
import kotlinx.datetime.LocalDate

sealed interface ViewScheduleScreenEvent : ScreenEvent {
    data class OnTaskClick(val taskId: String) : ViewScheduleScreenEvent
    data object OnNextWeekClick : ViewScheduleScreenEvent
    data object OnPrevWeekClick : ViewScheduleScreenEvent
    data class OnDateClick(val date: LocalDate) : ViewScheduleScreenEvent

    data object OnCreateTaskClick : ViewScheduleScreenEvent
    data object OnSearchClick : ViewScheduleScreenEvent

    sealed interface ResultEvent : ViewScheduleScreenEvent {
        val result: ScreenResult

        data class PickTaskType(
            override val result: PickTaskTypeScreenResult
        ) : ResultEvent

        data class PickTaskProgressType(
            override val result: PickTaskProgressTypeScreenResult
        ) : ResultEvent

        data class EnterTaskNumberRecord(
            override val result: EnterTaskNumberRecordScreenResult
        ) : ResultEvent

        data class EnterTaskTimeRecord(
            override val result: EnterTaskTimeRecordScreenResult
        ) : ResultEvent
    }
}