package com.example.inhabitroutine.domain.model.reminder

import com.example.inhabitroutine.domain.model.reminder.content.ReminderSchedule
import com.example.inhabitroutine.domain.model.reminder.type.ReminderType
import kotlinx.datetime.LocalTime

data class ReminderModel(
    val id: String,
    val taskId: String,
    val time: LocalTime,
    val type: ReminderType,
    val schedule: ReminderSchedule,
    val createdAt: Long
)
