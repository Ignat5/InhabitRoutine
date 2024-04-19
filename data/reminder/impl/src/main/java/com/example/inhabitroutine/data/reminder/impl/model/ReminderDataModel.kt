package com.example.inhabitroutine.data.reminder.impl.model

import com.example.inhabitroutine.domain.model.reminder.type.ReminderType
import kotlinx.datetime.LocalTime

data class ReminderDataModel(
    val id: String,
    val taskId: String,
    val reminderType: ReminderType,
    val reminderTime: LocalTime,
    val reminderSchedule: ReminderContentDataModel.ScheduleContent
)
