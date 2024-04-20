package com.example.inhabitroutine.feature.view_reminders.config.create_edit_reminder.base.components

import com.example.inhabitroutine.core.presentation.components.state.ScreenState
import com.example.inhabitroutine.domain.model.reminder.content.ReminderSchedule
import com.example.inhabitroutine.domain.model.reminder.type.ReminderType

interface BaseCreateEditReminderScreenState: ScreenState {
    val inputHours: Int
    val inputMinutes: Int
    val inputReminderType: ReminderType
    val inputReminderSchedule: ReminderSchedule
    val canConfirm: Boolean
}