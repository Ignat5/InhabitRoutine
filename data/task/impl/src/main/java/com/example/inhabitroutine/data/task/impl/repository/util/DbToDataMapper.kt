package com.example.inhabitroutine.data.task.impl.repository.util

import com.example.inhabitroutine.data.task.impl.repository.model.TaskContentEntity
import com.example.inhabitroutine.data.task.impl.repository.model.TaskEntity
import com.example.inhabitroutine.data.task.impl.repository.model.reminder.ReminderContentEntity
import com.example.inhabitroutine.data.task.impl.repository.model.reminder.ReminderEntity
import com.example.inhabitroutine.domain.model.reminder.type.ReminderType
import com.example.inhabitroutine.domain.model.task.type.TaskProgressType
import com.example.inhabitroutine.domain.model.task.type.TaskType
import database.TaskWithContentView
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json


// db -> data
internal fun TaskWithContentView.toTaskEntity(json: Json): TaskEntity? {
    return TaskEntity(
        id = this.task_id,
        type = this.task_type.decodeFromTaskType(json) ?: return null,
        progressType = this.task_progressType.decodeFromTaskProgressType(json) ?: return null,
        title = this.task_title,
        description = this.task_description,
        startDate = this.task_startEpochDay.toDateFromEpochDay(),
        endDate = this.task_endEpochDay.toDateFromEpochDay()
            .let { date -> if (date == distantFutureDate) null else date },
        progress = this.taskContent_progress.decodeFromTaskProgress(json) ?: return null,
        frequency = this.taskContent_frequency.decodeFromTaskFrequency(json) ?: return null,
        archive = this.taskContent_archive.decodeFromTaskArchive(json) ?: return null,
        reminder = this.toReminderEntity(json),
        versionSinceDate = this.taskContent_startEpochDay.toDateFromEpochDay(),
        createdAt = this.task_createdAt,
        deletedAt = this.task_deletedAt
    )
}

private fun TaskWithContentView.toReminderEntity(json: Json): ReminderEntity? {
    return ReminderEntity(
        id = reminder_id ?: return null,
        taskId = reminder_taskId ?: return null,
        time = reminder_time?.decodeFromTime(json) ?: return null,
        reminderType = reminder_type?.decodeFromReminderType(json) ?: return null,
        reminderSchedule = reminder_schedule?.decodeFromReminderSchedule(json) ?: return null,
        createdAt = reminder_createdAt ?: return null
    )
}

private fun String.decodeFromTaskProgress(json: Json): TaskContentEntity.ProgressContent? =
    runCatching {
        json.decodeFromString<TaskContentEntity.ProgressContent>(this)
    }.getOrNull()

private fun String.decodeFromTaskFrequency(json: Json): TaskContentEntity.FrequencyContent? =
    runCatching {
        json.decodeFromString<TaskContentEntity.FrequencyContent>(this)
    }.getOrNull()

private fun String.decodeFromTaskArchive(json: Json): TaskContentEntity.ArchiveContent? =
    runCatching {
        json.decodeFromString<TaskContentEntity.ArchiveContent>(this)
    }.getOrNull()

private fun String.decodeFromReminderSchedule(json: Json): ReminderContentEntity.ScheduleContent? =
    runCatching {
        json.decodeFromString<ReminderContentEntity.ScheduleContent>(this)
    }.getOrNull()

private fun String.decodeFromTime(json: Json): LocalTime? =
    runCatching {
        json.decodeFromString<LocalTime>(this)
    }.getOrNull()

private fun Long.toDateFromEpochDay(): LocalDate =
    LocalDate.fromEpochDays(this.toInt())

private val distantFutureDate: LocalDate by lazy {
    Instant.DISTANT_FUTURE.toLocalDateTime(TimeZone.currentSystemDefault()).date
}

private fun String.decodeFromTaskType(json: Json): TaskType? =
    runCatching {
        json.decodeFromString<TaskType>(this)
    }.getOrNull()

private fun String.decodeFromTaskProgressType(json: Json): TaskProgressType? =
    runCatching {
        json.decodeFromString<TaskProgressType>(this)
    }.getOrNull()

private fun String.decodeFromReminderType(json: Json): ReminderType? =
    runCatching {
        json.decodeFromString<ReminderType>(this)
    }.getOrNull()