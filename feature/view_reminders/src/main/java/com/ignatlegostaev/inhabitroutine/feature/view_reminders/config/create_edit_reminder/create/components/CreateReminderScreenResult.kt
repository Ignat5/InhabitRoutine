package com.ignatlegostaev.inhabitroutine.feature.view_reminders.config.create_edit_reminder.create.components

import com.ignatlegostaev.inhabitroutine.core.presentation.components.result.ScreenResult
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.content.ReminderSchedule
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.type.ReminderType
import kotlinx.datetime.LocalTime

sealed interface CreateReminderScreenResult : ScreenResult {
    data class Confirm(
        val reminderTime: LocalTime,
        val reminderType: ReminderType,
        val reminderSchedule: ReminderSchedule
    ) : CreateReminderScreenResult

    data object Dismiss : CreateReminderScreenResult
}