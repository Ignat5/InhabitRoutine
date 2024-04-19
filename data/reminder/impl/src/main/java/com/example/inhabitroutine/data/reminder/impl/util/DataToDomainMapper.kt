package com.example.inhabitroutine.data.reminder.impl.util

import com.example.inhabitroutine.data.reminder.impl.model.ReminderContentDataModel
import com.example.inhabitroutine.data.reminder.impl.model.ReminderDataModel
import com.example.inhabitroutine.domain.model.reminder.ReminderModel
import com.example.inhabitroutine.domain.model.reminder.content.ReminderSchedule

internal fun ReminderDataModel.toReminderModel() = ReminderModel(
    id = id,
    taskId = taskId,
    time = time,
    type = type,
    schedule = this.schedule.toReminderSchedule(),
    createdAt = createdAt
)

private fun ReminderContentDataModel.ScheduleContent.toReminderSchedule() =
    when (this) {
        is ReminderContentDataModel.ScheduleContent.AlwaysEnabled -> ReminderSchedule.AlwaysEnabled
        is ReminderContentDataModel.ScheduleContent.DaysOfWeek ->
            ReminderSchedule.DaysOfWeek(this.daysOfWeek)
    }