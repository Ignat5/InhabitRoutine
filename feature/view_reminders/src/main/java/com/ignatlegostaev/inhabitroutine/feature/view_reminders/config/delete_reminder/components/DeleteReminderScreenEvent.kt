package com.ignatlegostaev.inhabitroutine.feature.view_reminders.config.delete_reminder.components

import com.ignatlegostaev.inhabitroutine.core.presentation.components.event.ScreenEvent

sealed interface DeleteReminderScreenEvent : ScreenEvent {
    data object OnConfirmClick : DeleteReminderScreenEvent
    data object OnDismissRequest : DeleteReminderScreenEvent
}