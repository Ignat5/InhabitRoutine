package com.example.inhabitroutine.feature.view_reminders.config.create_edit_reminder.edit.components

import com.example.inhabitroutine.domain.model.reminder.content.ReminderSchedule
import com.example.inhabitroutine.domain.model.reminder.type.ReminderType
import com.example.inhabitroutine.feature.view_reminders.config.create_edit_reminder.base.components.BaseCreateEditReminderScreenState

data class EditReminderScreenState(
    override val inputHours: Int,
    override val inputMinutes: Int,
    override val inputReminderType: ReminderType,
    override val inputReminderSchedule: ReminderSchedule,
    override val canConfirm: Boolean
) : BaseCreateEditReminderScreenState
