package com.ignatlegostaev.inhabitroutine.data.reminder.impl.model

import com.ignatlegostaev.inhabitroutine.domain.model.reminder.type.ReminderType
import kotlinx.datetime.LocalTime

data class ReminderDataModel(
    val id: String,
    val taskId: String,
    val type: ReminderType,
    val time: LocalTime,
    val schedule: ReminderContentDataModel.ScheduleContent,
    val createdAt: Long
)
