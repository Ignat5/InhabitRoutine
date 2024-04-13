package com.example.inhabitroutine.data.task.impl.repository.model.reminder

import com.example.inhabitroutine.domain.model.reminder.type.ReminderType
import kotlinx.datetime.LocalTime

data class ReminderEntity(
    val id: String,
    val taskId: String,
    val time: LocalTime,
    val reminderType: ReminderType,
    val reminderSchedule: ReminderContentEntity.ScheduleContent,
    val createdAt: Long
)
