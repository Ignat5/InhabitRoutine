package com.example.inhabitroutine.data.reminder.impl.util

import com.example.inhabitroutine.core.database.reminder.api.ReminderEntity
import com.example.inhabitroutine.data.reminder.impl.model.ReminderContentDataModel
import com.example.inhabitroutine.data.reminder.impl.model.ReminderDataModel
import com.example.inhabitroutine.domain.model.reminder.type.ReminderType
import kotlinx.datetime.LocalTime
import kotlinx.serialization.json.Json

internal fun ReminderEntity.toReminderDataModel(json: Json): ReminderDataModel? {
    return ReminderDataModel(
        id = id,
        taskId = taskId,
        type = reminderType.decodeFromReminderType(json) ?: return null,
        time = reminderTime.decodeFromTime(json) ?: return null,
        schedule = reminderSchedule.decodeFromReminderSchedule(json) ?: return null,
        createdAt = createdAt
    )
}

private fun String.decodeFromReminderType(json: Json): ReminderType? = runCatching {
    json.decodeFromString<ReminderType>(this)
}.getOrNull()

private fun String.decodeFromTime(json: Json): LocalTime? = runCatching {
    json.decodeFromString<LocalTime>(this)
}.getOrNull()

private fun String.decodeFromReminderSchedule(json: Json): ReminderContentDataModel.ScheduleContent? =
    runCatching {
        json.decodeFromString<ReminderContentDataModel.ScheduleContent>(this)
    }.getOrNull()