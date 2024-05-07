package com.ignatlegostaev.inhabitroutine.feature.view_reminders.config.create_edit_reminder.edit.components

import com.ignatlegostaev.inhabitroutine.core.presentation.components.event.ScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.config.create_edit_reminder.base.components.BaseCreateEditReminderScreenEvent

sealed interface EditReminderScreenEvent : ScreenEvent {
    data class Base(
        val baseEvent: BaseCreateEditReminderScreenEvent
    ): EditReminderScreenEvent
}