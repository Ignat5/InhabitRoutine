package com.example.inhabitroutine.feature.search_tasks.components

import com.example.inhabitroutine.core.presentation.components.event.ScreenEvent

sealed interface SearchTasksScreenEvent : ScreenEvent {
    data class OnInputQueryUpdate(val value: String) : SearchTasksScreenEvent
    data class OnTaskClick(val taskId: String) : SearchTasksScreenEvent
    data object OnLeaveRequest : SearchTasksScreenEvent
}