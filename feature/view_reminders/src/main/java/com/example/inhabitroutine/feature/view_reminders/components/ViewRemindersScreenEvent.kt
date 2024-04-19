package com.example.inhabitroutine.feature.view_reminders.components

import com.example.inhabitroutine.core.presentation.components.event.ScreenEvent

sealed interface ViewRemindersScreenEvent : ScreenEvent {
    data object OnLeaveRequest : ViewRemindersScreenEvent
}