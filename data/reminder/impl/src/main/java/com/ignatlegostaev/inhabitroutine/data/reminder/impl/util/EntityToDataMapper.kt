package com.ignatlegostaev.inhabitroutine.data.reminder.impl.util

import com.ignatlegostaev.inhabitroutine.core.database.reminder.api.ReminderEntity
import com.ignatlegostaev.inhabitroutine.data.reminder.impl.model.ReminderContentDataModel
import com.ignatlegostaev.inhabitroutine.data.reminder.impl.model.ReminderDataModel
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.type.ReminderType
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal fun ReminderEntity.toReminderDataModel(json: Json): ReminderDataModel? {
    return ReminderDataModel(
        id = id,
        taskId = taskId,
        type = type.decodeFromReminderType(json) ?: return null,
        time = time.decodeFromTime(json) ?: return null,
        schedule = schedule.decodeFromReminderSchedule(json) ?: return null,
        createdAt = createdAt
    )
}

internal fun ReminderDataModel.toReminderEntity(json: Json): ReminderEntity? {
    return ReminderEntity(
        id = id,
        taskId = taskId,
        type = type.encodeToString(json) ?: return null,
        time = time.encodeToString(json) ?: return null,
        schedule = schedule.encodeToString(json) ?: return null,
        createdAt = createdAt
    )
}

/* reminder type */
private fun ReminderType.encodeToString(json: Json) = runCatching {
    json.encodeToString(this)
}.getOrNull()

private fun String.decodeFromReminderType(json: Json): ReminderType? = runCatching {
    json.decodeFromString<ReminderType>(this)
}.getOrNull()

/* reminder time */
private fun LocalTime.encodeToString(json: Json) = runCatching {
    json.encodeToString(this)
}.getOrNull()

private fun String.decodeFromTime(json: Json): LocalTime? = runCatching {
    json.decodeFromString<LocalTime>(this)
}.getOrNull()

/* date */
internal fun LocalDate.encodeToEpochDay() = this.toEpochDays().toLong()

/* reminder schedule */
private fun ReminderContentDataModel.ScheduleContent.encodeToString(json: Json) = runCatching {
    json.encodeToString(this)
}.getOrNull()

private fun String.decodeFromReminderSchedule(json: Json): ReminderContentDataModel.ScheduleContent? =
    runCatching {
        json.decodeFromString<ReminderContentDataModel.ScheduleContent>(this)
    }.getOrNull()