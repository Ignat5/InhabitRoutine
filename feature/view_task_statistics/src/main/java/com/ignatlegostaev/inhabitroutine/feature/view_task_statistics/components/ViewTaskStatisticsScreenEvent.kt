package com.ignatlegostaev.inhabitroutine.feature.view_task_statistics.components

import com.ignatlegostaev.inhabitroutine.core.presentation.components.event.ScreenEvent

sealed interface ViewTaskStatisticsScreenEvent : ScreenEvent {
    data object OnLeaveRequest : ViewTaskStatisticsScreenEvent
}