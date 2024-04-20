package com.example.inhabitroutine.feature.view_reminders.config.create_edit_reminder.create.components

import com.example.inhabitroutine.core.presentation.components.event.ScreenEvent
import com.example.inhabitroutine.feature.view_reminders.config.create_edit_reminder.base.components.BaseCreateEditReminderScreenEvent

sealed interface CreateReminderScreenEvent : ScreenEvent {
    data class Base(
        val baseEvent: BaseCreateEditReminderScreenEvent
    ) : CreateReminderScreenEvent
}