package com.example.inhabitroutine.data.task.impl.repository.util

import com.example.inhabitroutine.core.database.api.model.core.ReminderEntity
import com.example.inhabitroutine.core.database.api.model.core.TaskContentEntity
import com.example.inhabitroutine.core.database.api.model.core.TaskEntity
import com.example.inhabitroutine.core.database.api.model.derived.TaskWithContentEntity
import com.example.inhabitroutine.data.task.impl.repository.model.reminder.ReminderContentDataModel
import com.example.inhabitroutine.data.task.impl.repository.model.reminder.ReminderDataModel
import com.example.inhabitroutine.data.task.impl.repository.model.task.TaskContentDataModel
import com.example.inhabitroutine.data.task.impl.repository.model.task.TaskDataModel
import com.example.inhabitroutine.domain.model.reminder.type.ReminderType
import com.example.inhabitroutine.domain.model.task.type.TaskProgressType
import com.example.inhabitroutine.domain.model.task.type.TaskType
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal fun TaskWithContentEntity.toTaskDataModel(json: Json): TaskDataModel? {
    return TaskDataModel(
        id = this.task.id,
        type = this.task.type.decodeFromTaskType(json) ?: return null,
        progressType = this.task.progressType.decodeFromTaskProgressType(json) ?: return null,
        title = this.task.title,
        description = this.task.description,
        startDate = this.task.startEpochDay.toDateFromEpochDay(),
        endDate = this.task.endEpochDay.toDateFromEpochDay()
            .let { date -> if (date != distantFutureDate) date else null },
        progress = this.taskContent.progress.decodeFromTaskProgress(json) ?: return null,
        frequency = this.taskContent.frequency.decodeFromTaskFrequency(json) ?: return null,
        archive = this.taskContent.archive.decodeFromTaskArchive(json) ?: return null,
        reminder = this.reminder?.toReminderDataModel(json),
        versionSinceDate = this.taskContent.startEpochDay.toDateFromEpochDay(),
        createdAt = this.task.createdAt,
        deletedAt = this.task.deletedAt
    )
}

private fun ReminderEntity.toReminderDataModel(json: Json): ReminderDataModel? {
    return ReminderDataModel(
        id = this.id,
        taskId = this.taskId,
        time = this.time.decodeFromTime(json) ?: return null,
        reminderType = this.type.decodeFromReminderType(json) ?: return null,
        reminderSchedule = this.schedule.decodeFromReminderSchedule(json) ?: return null,
        createdAt = this.createdAt
    )
}

internal fun TaskDataModel.toTaskEntity(json: Json): TaskEntity? = runCatching {
    TaskEntity(
        id = this.id,
        type = this.type.encodeToString(json) ?: return@runCatching null,
        progressType = this.progressType.encodeToString(json) ?: return@runCatching null,
        title = this.title,
        description = this.description,
        startEpochDay = this.startDate.encodeToEpochDay(),
        endEpochDay = (this.endDate ?: distantFutureDate).encodeToEpochDay(),
        createdAt = this.createdAt,
        deletedAt = this.deletedAt
    )
}.getOrNull()

//internal fun TaskDataModel.toTaskContentEntity(json: Json): TaskContentEntity? = runCatching {
//    TaskContentEntity(
//        id = ,
//        taskId = ,
//        progress = ,
//        frequency = ,
//        archive = ,
//        startEpochDay = ,
//        createdAt =
//    )
//}.getOrNull()

private fun String.decodeFromTaskProgress(json: Json): TaskContentDataModel.ProgressContent? =
    runCatching {
        json.decodeFromString<TaskContentDataModel.ProgressContent>(this)
    }.getOrNull()

private fun String.decodeFromTaskFrequency(json: Json): TaskContentDataModel.FrequencyContent? =
    runCatching {
        json.decodeFromString<TaskContentDataModel.FrequencyContent>(this)
    }.getOrNull()

private fun String.decodeFromTaskArchive(json: Json): TaskContentDataModel.ArchiveContent? =
    runCatching {
        json.decodeFromString<TaskContentDataModel.ArchiveContent>(this)
    }.getOrNull()

private fun String.decodeFromReminderSchedule(json: Json): ReminderContentDataModel.ScheduleContent? =
    runCatching {
        json.decodeFromString<ReminderContentDataModel.ScheduleContent>(this)
    }.getOrNull()

/* time */

private fun LocalDate.encodeToEpochDay(): Long =
    this.toEpochDays().toLong()

private fun String.decodeFromTime(json: Json): LocalTime? =
    runCatching {
        json.decodeFromString<LocalTime>(this)
    }.getOrNull()

private fun Long.toDateFromEpochDay(): LocalDate =
    LocalDate.fromEpochDays(this.toInt())

private val distantFutureDate: LocalDate by lazy {
    Instant.DISTANT_FUTURE.toLocalDateTime(TimeZone.currentSystemDefault()).date
}

/* task type */
private fun TaskType.encodeToString(json: Json): String? = runCatching {
    json.encodeToString(this)
}.getOrNull()

private fun String.decodeFromTaskType(json: Json): TaskType? =
    runCatching {
        json.decodeFromString<TaskType>(this)
    }.getOrNull()

/* task progress type */

private fun TaskProgressType.encodeToString(json: Json): String? =
    runCatching {
        json.encodeToString(this)
    }.getOrNull()

private fun String.decodeFromTaskProgressType(json: Json): TaskProgressType? =
    runCatching {
        json.decodeFromString<TaskProgressType>(this)
    }.getOrNull()

private fun String.decodeFromReminderType(json: Json): ReminderType? =
    runCatching {
        json.decodeFromString<ReminderType>(this)
    }.getOrNull()