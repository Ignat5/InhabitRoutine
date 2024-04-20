package com.example.inhabitroutine.feature.view_reminders.config.create_edit_reminder.edit.components

import com.example.inhabitroutine.core.presentation.components.result.ScreenResult
import com.example.inhabitroutine.domain.model.reminder.ReminderModel

sealed interface EditReminderScreenResult : ScreenResult {
    data class Confirm(
        val reminderModel: ReminderModel
    ) : EditReminderScreenResult

    data object Dismiss : EditReminderScreenResult
}