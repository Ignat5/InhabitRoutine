package com.example.inhabitroutine.feature.view_reminders.config.delete_reminder.components

import com.example.inhabitroutine.core.presentation.components.result.ScreenResult

sealed interface DeleteReminderScreenResult : ScreenResult {
    data class Confirm(
        val reminderId: String
    ) : DeleteReminderScreenResult

    data object Dismiss : DeleteReminderScreenResult
}