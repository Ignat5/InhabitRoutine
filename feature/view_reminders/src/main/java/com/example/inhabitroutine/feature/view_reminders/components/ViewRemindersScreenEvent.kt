package com.example.inhabitroutine.feature.view_reminders.components

import com.example.inhabitroutine.core.presentation.components.event.ScreenEvent
import com.example.inhabitroutine.core.presentation.components.result.ScreenResult
import com.example.inhabitroutine.feature.view_reminders.config.create_edit_reminder.create.components.CreateReminderScreenResult

sealed interface ViewRemindersScreenEvent : ScreenEvent {
    data object OnCreateReminderClick : ViewRemindersScreenEvent
    data object OnMessageShown : ViewRemindersScreenEvent
    data object OnLeaveRequest : ViewRemindersScreenEvent

    sealed interface ResultEvent : ViewRemindersScreenEvent {
        val result: ScreenResult

        data class CreateReminder(
            override val result: CreateReminderScreenResult
        ) : ResultEvent
    }
}