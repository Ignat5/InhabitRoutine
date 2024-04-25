package com.example.inhabitroutine.feature.view_task_statistics.components

import com.example.inhabitroutine.core.presentation.components.event.ScreenEvent

sealed interface ViewTaskStatisticsScreenEvent : ScreenEvent {
    data object OnLeaveRequest : ViewTaskStatisticsScreenEvent
}